package core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SqlProcessor {

    public static void execute(String template, String rawData, String outDirStr,
                               String prefix, int batchSize, int maxLinesFile, Consumer<String> logger) {
        new Thread(() -> {
            try {
                long start = System.currentTimeMillis();
                logger.accept("📊 Đang tiến hành chuẩn hóa điều kiện dữ liệu SQL UNION ALL...");

                final String activeTemplate = template.replaceAll("\\(\\s*\\$\\{itemset\\}\\s*\\)", "\\${itemset}");
                Path targetDir = Paths.get(outDirStr);
                if (!Files.exists(targetDir)) Files.createDirectories(targetDir);

                String[] allLines = rawData.split("\\r?\\n");
                List<String> sqlBlocks = new ArrayList<>();
                List<String> currentBatch = new ArrayList<>(batchSize);
                int currentFileLines = 0;
                int fileIdx = 1;

                for (String currentLine : allLines) {
                    String clean = currentLine.trim();
                    if (clean.isEmpty()) continue;

                    currentBatch.add(clean);
                    currentFileLines++;

                    if (currentBatch.size() == batchSize) {
                        sqlBlocks.add(generateSmartSqlBlock(activeTemplate, currentBatch));
                        currentBatch.clear();
                    }

                    if (currentFileLines >= maxLinesFile) {
                        if (!currentBatch.isEmpty()) {
                            sqlBlocks.add(generateSmartSqlBlock(activeTemplate, currentBatch));
                            currentBatch.clear();
                        }
                        saveSqlToFile(targetDir, prefix, fileIdx, sqlBlocks);
                        logger.accept(String.format("→ Đã kết xuất: %s/%s%03d.txt (%d dòng điều kiện)", outDirStr, prefix, fileIdx, currentFileLines));
                        sqlBlocks.clear();
                        currentFileLines = 0;
                        fileIdx++;
                    }
                }

                if (!currentBatch.isEmpty()) {
                    sqlBlocks.add(generateSmartSqlBlock(activeTemplate, currentBatch));
                }
                if (!sqlBlocks.isEmpty()) {
                    saveSqlToFile(targetDir, prefix, fileIdx, sqlBlocks);
                    logger.accept(String.format("→ Đã kết xuất: %s/%s%03d.txt (%d dòng - Khối cuối)", outDirStr, prefix, fileIdx, currentFileLines));
                }
                logger.accept(String.format("🏁 [HOÀN THÀNH] Chuẩn hóa và phân tách các câu lệnh SQL hoàn tất sau: %d ms!", (System.currentTimeMillis() - start)));
            } catch (Exception ex) {
                logger.accept("❌ [HỆ THỐNG PHÁT SINH LỖI LUỒNG]: " + ex.getMessage());
            }
        }).start();
    }

    private static String generateSmartSqlBlock(String safeTemplate, List<String> batchData) {
        StringBuilder sb = new StringBuilder();
        sb.append("(\n");
        for (int i = 0; i < batchData.size(); i++) {
            sb.append("    '").append(batchData.get(i).replace("'", "''")).append("'");
            if (i < batchData.size() - 1) sb.append(",\n");
            else sb.append("\n");
        }
        sb.append(")");
        return safeTemplate.replace("${itemset}", sb.toString());
    }

    private static void saveSqlToFile(Path outDir, String prefix, int fileIndex, List<String> sqlBlocks) throws IOException {
        String combined = String.join("\n\nUNION ALL\n\n", sqlBlocks);
        String filename = prefix + String.format("%03d.txt", fileIndex);
        try (BufferedWriter bw = Files.newBufferedWriter(outDir.resolve(filename), StandardCharsets.UTF_8)) {
            bw.write(combined);
        }
    }
}
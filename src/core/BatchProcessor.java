package core;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class BatchProcessor {
    public static void execute(String template, String rawData, String outDirStr,
                               String prefix, String sleepCmd, int chunkSize,
                               int linesPerFile, Consumer<String> logger) {
        new Thread(() -> {
            try {
                long start = System.currentTimeMillis();
                logger.accept("🚀 Đang chạy bộ xử lý đóng gói tệp tin Batch Message...");

                Path targetDir = Paths.get(outDirStr);
                if (!Files.exists(targetDir)) Files.createDirectories(targetDir);

                String[] allLines = rawData.split("\\r?\\n");
                List<String> currentChunk = new ArrayList<>(chunkSize);
                int fileIdx = 1;
                int currentFileLines = 0;
                BufferedWriter bw = null;

                for (String currentLine : allLines) {
                    String clean = currentLine.trim();
                    if (clean.isEmpty()) continue;
                    currentChunk.add(clean);

                    if (currentChunk.size() == chunkSize) {
                        if (bw == null) {
                            bw = Files.newBufferedWriter(targetDir.resolve(prefix + String.format("%03d.txt", fileIdx)), StandardCharsets.UTF_8);
                        }
                        bw.write(template.replace("${itemset}", String.join(System.lineSeparator(), currentChunk)));
                        bw.newLine();
                        bw.write(sleepCmd);
                        bw.newLine();
                        bw.newLine();

                        currentFileLines += chunkSize;
                        currentChunk.clear();

                        if (currentFileLines >= linesPerFile) {
                            bw.close();
                            logger.accept(String.format("→ Đã kết xuất thành công: %s/%s%03d.txt (%d dòng)", outDirStr, prefix, fileIdx, currentFileLines));
                            bw = null;
                            fileIdx++;
                            currentFileLines = 0;
                        }
                    }
                }

                if (!currentChunk.isEmpty()) {
                    if (bw == null) {
                        bw = Files.newBufferedWriter(targetDir.resolve(prefix + String.format("%03d.txt", fileIdx)), StandardCharsets.UTF_8);
                    }
                    bw.write(template.replace("${itemset}", String.join(System.lineSeparator(), currentChunk)));
                    bw.newLine();
                    bw.write(sleepCmd);
                    bw.newLine();
                    currentFileLines += currentChunk.size();
                }

                if (bw != null) {
                    bw.close();
                    logger.accept(String.format("→ Đã kết xuất thành công: %s/%s%03d.txt (%d dòng - Khối cuối)", outDirStr, prefix, fileIdx, currentFileLines));
                }
                logger.accept(String.format("🏁 [HOÀN THÀNH] Toàn bộ tệp tin Batch hoàn tất trong: %d ms!", (System.currentTimeMillis() - start)));
            } catch (Exception ex) {
                logger.accept("❌ [HỆ THỐNG PHÁT SINH LỖI LUỒNG]: " + ex.getMessage());
            }
        }).start();
    }
}
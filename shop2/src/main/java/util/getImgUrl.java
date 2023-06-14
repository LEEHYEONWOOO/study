package util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class getImgUrl {

    public static void main(String[] args) throws IOException {
        // URL 주소를 입력합니다.
        URL url = new URL("http://mlanding.co.kr/mf/7lotto/type1/?ads_id=MTkzOXxffDEzNnxffDg0NnxffDQ1NzkxNg==");

        // HTTP 연결을 엽니다.
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // 연결 상태가 200 OK인지 확인합니다.
        if (connection.getResponseCode() != 200) {
            throw new IOException("Invalid response code: " + connection.getResponseCode());
        }

        // 연결에서 이미지 태그를 찾습니다.
        Scanner scanner = new Scanner(connection.getInputStream());
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.contains("<img")) {
                // 이미지 태그를 찾았습니다.
                // 태그에서 src 속성을 추출합니다.
                String src = line.substring(line.indexOf("src=") + 5, line.indexOf(">"));

                // src 속성의 값을 사용하여 이미지를 다운로드합니다.
                //byte[] imageData = downloadImage(src);

                // 이미지를 표시합니다.
                System.out.println(src);
            }
        }
    }
}
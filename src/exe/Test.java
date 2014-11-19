package exe;

import java.io.IOException;
import java.util.Scanner;

import utils.NetworkUtils;
import core.AnalyzeMemcache;

public class Test {
    public static void main(String[] args) {
        AnalyzeMemcache analyzeMemcache = AnalyzeMemcache.getInstance();
        System.out.println("*****************************************");
        try {
            boolean flag = true;
            Scanner scanner = new Scanner(System.in);
            StringBuilder commmandBuilder = new StringBuilder();
            System.out.println("please tell me the address you want to connect : ");

            if (scanner.hasNextLine()) {
                commmandBuilder.append(scanner.nextLine());
            }
            String[] addressArr = commmandBuilder.toString().split(" ");
            String ip = NetworkUtils.getValidIp(addressArr[0]);
            Integer port = NetworkUtils.getValidPort(addressArr[1]);
            analyzeMemcache.connect(ip, port);

            System.out.println("please input your command : ");
            while (flag) {
                Inner: while (scanner.hasNextLine()) {
                    scanner.reset();
                    commmandBuilder.setLength(0);
                    commmandBuilder.append(scanner.nextLine());
                    analyzeMemcache.execute(commmandBuilder.toString());
                    if ("quit".equals(commmandBuilder.toString())) {
                        flag = false;
                        break;
                    }
                    System.out.println("please input your command : ");
                    break Inner;
                }
            }
            scanner.close();
            analyzeMemcache.disConnect();
            System.out.println("finish");
            System.out.println("*****************************************");
            System.exit(0);
            // analyzeMemcache.execute("stats cachedump 2 0");
            // analyzeMemcache.execute("get app_icon_list_293351");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

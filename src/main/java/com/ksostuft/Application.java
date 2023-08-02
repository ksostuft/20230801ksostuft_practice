package com.ksostuft;

import java.util.Scanner;

import static com.ksostuft.common.JDBCTemplate.close;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. 조회 | 2. 추가 | 3. 변경 | 4. 제거 | 5. 종료");
        System.out.println();

        while(true) {
            int selectNo = 0;
            try {
                System.out.print("원하는 메뉴를 선택하세요: ");
                selectNo = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println("입력이 올바르지 않습니다.");
                System.out.println();
                continue;
            }
            if(selectNo > 5 || selectNo < 1) {
                System.out.println();
                System.out.println("입력이 올바르지 않습니다.");
                System.out.println();
                continue;
            }
            switch (selectNo) {
                case 1:
                    CRUD.select();
                    break;
                case 2:
                    CRUD.insert();
                    break;
                case 3:
                    CRUD.update();
                    break;
                case 4:
                    CRUD.delete();
                    break;
                case 5:
                    System.out.println("종료합니다.");
                    return;
            }
        }
    }
}

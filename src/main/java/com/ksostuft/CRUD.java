package com.ksostuft;

import com.ksostuft.model.dto.MenuDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import static com.ksostuft.common.JDBCTemplate.close;
import static com.ksostuft.common.JDBCTemplate.getConnection;

public class CRUD {
    public static void select() {
        Connection con = getConnection();
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Properties prop = new Properties();


        try {
            prop.loadFromXML(new FileInputStream("src/main/java/com/ksostuft/mapper/menu-query.xml"));
            String query = prop.getProperty("selectMenu");
            pstmt = con.prepareStatement(query);
            String orderStatus = "";
            while(true) {
                System.out.printf("주문 상태 여부를 선택해주세요 (Y|N): ");
                Scanner scanner = new Scanner(System.in);
                orderStatus = scanner.nextLine().toUpperCase();
                if(!orderStatus.equals("Y") && !orderStatus.equals("N")) {
                    System.out.println("입력이 올바르지 않습니다.");
                    System.out.println("다시 입력해주세요");
                } else {
                    break;
                }
            }
            if(orderStatus.equals("Y")) {
                System.out.println("주문 가능한 메뉴를 출력합니다.");
                pstmt.setString(1, "Y");
            } else {
                System.out.println("주문 불가능한 메뉴를 출력합니다.");
                pstmt.setString(1, "N");
            }
            rset = pstmt.executeQuery();
            ArrayList<MenuDTO> dtoList = new ArrayList<>();
            while(rset.next()) {
                String menuName = rset.getString("MENU_NAME");
                int menuPrice = Integer.parseInt(rset.getString("MENU_PRICE"));
                int categoryCode = Integer.parseInt(rset.getString("CATEGORY_CODE"));
                String orderableStatus = rset.getString("ORDERABLE_STATUS");
                MenuDTO dto = new MenuDTO(menuName, menuPrice, categoryCode, orderableStatus);
                dtoList.add(dto);
            }
            String columnOne = "메뉴 이름";
            String columnTwo = "메뉴 가격";
            String columnThree = "카테고리 코드";
            String columnFour = "주문 가능 여부";

            System.out.printf("%10s | %15s | %15s | %15s\n", columnOne, columnTwo, columnThree, columnFour);
            for(MenuDTO dto : dtoList) {
                System.out.printf("%10s | %15s | %15s | %15s\n", dto.getMenuName(), dto.getMenuPrice(), dto.getCategoryCode(), dto.getOrderableStatus());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    public static void insert() {
        Connection con = getConnection();
        PreparedStatement pstmt = null;
        int result = 0;
        Properties prop = new Properties();

        try {
            prop.loadFromXML(new FileInputStream("src/main/java/com/ksostuft/mapper/menu-query.xml"));
            String query = prop.getProperty("insertMenu");
            pstmt = con.prepareStatement(query);
            Scanner scanner = new Scanner(System.in);
            System.out.print("메뉴 이름을 입력하세요: ");
            String menuName = scanner.nextLine();
            System.out.print("메뉴 가격을 입력하세요: ");
            int menuPrice = 0;
            try {
                menuPrice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다.");
                close(pstmt);
                close(con);
                return;
            }
            System.out.print("카테고리 코드를 입력하세요: ");
            int cateCode = 0;
            try {
                cateCode = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다.");
                close(pstmt);
                close(con);
                return;
            }
            if(cateCode <1 || cateCode > 5) {
                System.out.println("잘못된 입력입니다.");
                close(pstmt);
                close(con);
                return;
            }
            System.out.print("주문가능 여부 입력하세요: ");
            String orderStatus = "";
            while(true) {
                System.out.printf("주문 상태 여부를 선택해주세요 (Y|N): ");
                orderStatus = scanner.nextLine().toUpperCase();
                if(!orderStatus.equals("Y") && !orderStatus.equals("N")) {
                    System.out.println("올바르지 않은 값이 입력되었습니다.");
                    return;
                }
                break;
            }
            System.out.print("잘못 입력한 값이 있을 경우 Y를 입력하세요: ");
            String checkWrong = scanner.nextLine().toUpperCase();
            if(checkWrong.equals("Y")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }


            MenuDTO dto = new MenuDTO(menuName, menuPrice, cateCode, orderStatus);

            pstmt.setString(1, dto.getMenuName());
            pstmt.setInt(2, dto.getMenuPrice());
            pstmt.setInt(3, dto.getCategoryCode());
            pstmt.setString(4, dto.getOrderableStatus());

            result = pstmt.executeUpdate();
            System.out.println(result + "개 행을 추가했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
            close(con);
        }


    }
    public static void update() {
        Connection con = getConnection();
        PreparedStatement pstmt = null;
        int result = 0;
        Properties prop = new Properties();
        try {
            prop.loadFromXML(new FileInputStream("src/main/java/com/ksostuft/mapper/menu-query.xml"));
            String query = prop.getProperty("updateMenu");
            pstmt = con.prepareStatement(query);
            Scanner scanner = new Scanner(System.in);
            int updateNum = 0;
            try {
                System.out.printf("갱신하고 싶은 행의 번호를 입력하세요: ");
                updateNum = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다.");
                close(pstmt);
                close(con);
                return;
            }
            System.out.printf("바꿀 메뉴의 이름을 입력하세요: ");
            String changeMenuName = scanner.nextLine();
            int changePrice = 0;
            try {
                System.out.printf("바꿀 메뉴 가격을 입력하세요: ");
                changePrice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다.");
                close(pstmt);
                close(con);
                return;
            }
            System.out.print("잘못 입력한 값이 있을 경우 Y를 입력하세요: ");
            String checkWrong = scanner.nextLine().toUpperCase();
            if(checkWrong.equals("Y")) {
                System.out.println("메뉴로 돌아갑니다.");
                return;
            }

            MenuDTO dto = new MenuDTO();
            dto.setMenuCode(updateNum);
            dto.setMenuName(changeMenuName);
            dto.setMenuPrice(changePrice);
            pstmt.setString(1, dto.getMenuName());
            pstmt.setInt(2, dto.getMenuPrice());
            pstmt.setInt(3, dto.getMenuCode());

            result = pstmt.executeUpdate();
            System.out.println(result + "개의 행을 갱신했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(con);
            close(pstmt);
        }


    }
    public static void delete() {
        Connection con = getConnection();
        PreparedStatement pstmt = null;
        int result = 0;
        Properties prop = new Properties();

        try {
            prop.loadFromXML(new FileInputStream("src/main/java/com/ksostuft/mapper/menu-query.xml"));
            String query = prop.getProperty("deleteMenu");
            pstmt = con.prepareStatement(query);
            Scanner scanner = new Scanner(System.in);
            int deleteCode = 0;
            try {
                System.out.print("제거하고 싶은 행의 번호를 입력하세요: ");
                deleteCode = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다.");
                close(pstmt);
                close(con);
                return;
            }
            MenuDTO dto = new MenuDTO();
            dto.setMenuCode(deleteCode);
            pstmt.setInt(1, dto.getMenuCode());
            result = pstmt.executeUpdate();
            System.out.println(result + "개의 행을 제거했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(pstmt);
            close(con);
        }


    }
}

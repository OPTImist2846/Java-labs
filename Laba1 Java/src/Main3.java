public class Main3 {
    public static void main(String[] args) {
        int mark = 4;

        switch (mark) {
            case 5:
                System.out.println("Ваша оцінка: Відмінно!");
                break;
            case 4:
                System.out.println("Ваша оцінка: Добре.");
                break;
            case 3:
                System.out.println("Ваша оцінка: Задовільно.");
                break;
            case 2:
            case 1:
                System.out.println("Ваша оцінка: Незадовільно.");
                break;
            default:
                System.out.println("Помилка: Введіть оцінку від 1 до 5.");
                break;
        }
    }
}
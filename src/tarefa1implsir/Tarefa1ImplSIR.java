package tarefa1implsir;

import java.util.Scanner;

public class Tarefa1ImplSIR {

    public static Scanner sn = new Scanner(System.in);
    
    public static void main(String[] args) {
        showHeader();
        
        boolean finalizar = false;
        while (!finalizar) {
            int opt = requestOption();
            switch (opt) {
                case 1:
                    solucaoQuestao4();
                    break;
                case 99:
                    finalizar = true;
                    break;
            }
        }
    }
    
    public static void showHeader() {
        System.out.println("INE5680 - Segurança da Informação e de Redes");
        System.out.println("Discentes: Diogo Fontana Nandi Machado (16200895)");
        System.out.println("           Flávio Silvino (16200899)");
        System.out.println("");
    }
    
    public static int requestOption() {
        System.out.println("Escolha uma opção:");
        System.out.println("  1) Questão 4 - Cifrar/decifrar string");
        System.out.println(" 99) Sair");
        System.out.println("");
        while (true) {
            System.out.print("Digite o número: ");
            String n = sn.nextLine();
            try {
                int opt = Integer.parseInt(n);
                if (opt >= 1 && opt <= 3 || opt == 99) {
                    System.out.println("");
                    return opt;
                } else {
                    System.out.println("Opção inválida. Digite um dos números da lista.");
                }
            } catch (Exception e) {
                System.out.println("Número inválido!");
            }
        }
    }

    private static void solucaoQuestao4() {
        new Questao4e5();
    }
    

    
}

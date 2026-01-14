package project;

/**
 * Classe App - Ponto de entrada da aplicacao
 * Esta e a classe principal que inicia o jogo do Titanic
 */
public class App {
    /**
     * Metodo main - Ponto de entrada do programa
     * Cria e exibe a janela de boas-vindas quando a aplicacao e executada
     * @param args Argumentos da linha de comandos (nao utilizados)
     */
    public static void main(String[] args) {
        // Cria uma nova janela de boas-vindas para iniciar o jogo
        new WelcomeWindow();
    }
}

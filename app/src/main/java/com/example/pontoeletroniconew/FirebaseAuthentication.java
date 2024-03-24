package com.example.pontoeletroniconew;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthentication {

    // Função para autenticar o usuário
    public static String authenticateUser(String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        try {
            // Autentica o usuário com email e senha
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Autenticação bem-sucedida, retorna o UID do usuário
                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid = user.getUid();
                            System.out.println("Usuário autenticado com sucesso. UID: " + uid);
                        } else {
                            // Falha na autenticação, exibe mensagem de erro
                            System.out.println("Falha na autenticação do usuário: " + task.getException());
                        }
                    });
        } catch (Exception e) {
            System.out.println("Erro ao autenticar usuário: " + e.getMessage());
        }

        return null;
    }

    // Exemplo de uso
    //public static void main(String[] args) {
    //    String userEmail = "pontoeletronicoaj@gmail.com";
    //    String userPassword = "2AJ@eletronico";
    //    authenticateUser(userEmail, userPassword);
    //}
}
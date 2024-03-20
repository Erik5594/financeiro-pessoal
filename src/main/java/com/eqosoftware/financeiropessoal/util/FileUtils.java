package com.eqosoftware.financeiropessoal.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@Slf4j
@Component
public class FileUtils {

    private static final String PATH_BASE = "%sfinan-pessoal%s";

    public static void escrever(String nomeArquivo, byte[] bytes) {
        try (FileOutputStream writer = new FileOutputStream(nomeArquivo)) {
            writer.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean criarArquivo(String path, String nome) throws IOException {
        if(criarPastas(path)) {
            var arquivo = new File(path + nome);
            if (arquivo.exists() && (arquivo.delete())) {
                return criarArquivo(path, nome);
            }
            return arquivo.createNewFile();
        }
        return false;
    }

    public static File criarImagemPerfil(String tenantId) throws IOException {
        var path = "%s%s%s%s%s".formatted(
                PATH_BASE.formatted(System.getProperty("file.separator"), System.getProperty("file.separator")),
                "images",
                System.getProperty("file.separator"),
                tenantId,
                System.getProperty("file.separator"));

        if(criarPastas(path)){
            var nomeArquivo = "perfil.jpg";
            var arquivo = new File(path + nomeArquivo);
            if (arquivo.exists() && (arquivo.delete())) {
                return criarImagemPerfil(tenantId);
            }
            return arquivo.createNewFile() ? arquivo : null;
        }
        return null;
    }

    public static boolean excluirImagemPerfil(String tenantId) {
        var path = "%s%s%s%s%s%s".formatted(
                PATH_BASE.formatted(System.getProperty("file.separator"), System.getProperty("file.separator")),
                "images",
                System.getProperty("file.separator"),
                tenantId,
                System.getProperty("file.separator"),
                "perfil.jpg");

        var imagem = new File(path);
        return imagem.exists() && imagem.delete();
    }

    private static boolean criarPastas(String caminhoCompleto){
        var diretorio = new File(caminhoCompleto);
        return diretorio.exists() || new File(caminhoCompleto).mkdirs();
    }

    public static void escreverImagem(String nomeArquivo, byte[] bytes) throws IOException {
        try (FileOutputStream writer = new FileOutputStream(nomeArquivo)) {
            writer.write(bytes);
        } catch (IOException e) {
            log.error("Ocorreu um erro ao tentar salvar imagem de perfil: ", e);
        }
    }

    public static String imageFileToBase64(File imageFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(imageFile)) {
            byte[] bytes = new byte[(int) imageFile.length()];
            fis.read(bytes);
            return Base64.getEncoder().encodeToString(bytes);
        }
    }

}

package br.ueg.tcc.bookway.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;

import br.com.vexillum.control.manager.ExceptionManager;

/**
 * Classe funcionalidades relacionadas a manipulação dos arquivos nos diretórios
 * para manutenção do repositório do texto
 * 
 * @author Pedro
 * 
 */
public class FolderUtils {

	/**
	 * Cria um repositório dentro do repositório padrão do sistema
	 * 
	 * @param path
	 *            , caminho que será utilizado para criação do diretório
	 * @param nameFolder
	 *            , nome do diretório a ser criado.
	 */
	public static void createFolder(String path, String nameFolder) {
		String separator = System.getProperty("file.separator");
		String directoryName = path + separator + nameFolder;
		try {
			if (!new File(directoryName).exists())
				(new File(directoryName)).mkdir();
		} catch (Exception e) {
			new ExceptionManager(e).treatException();
		}
	}

	/**
	 * Deleta um arquivo de um diretório específico
	 * 
	 * @param directoryUser
	 *            , diretório do qual será removido o arquivo
	 * @param filePath
	 *            , caminho completo do arquivo que será deletado
	 * @return verdadeiro se remover o arquivo com sucesso
	 */
	public static boolean deleteFileFolder(String directoryUser, String filePath) {
		boolean result = false;
		File dir = new File(filePath);
		if (dir.exists()) {
			try {
				FileUtils.forceDelete(dir);
				result = true;
			} catch (IOException e) {
				e.printStackTrace();
				new ExceptionManager(e).treatException();
			}
		}
		checkDiretoryUser(directoryUser);
		return result;
	}

	/**
	 * Verifica a existencia de um diretório e se existir o remove.
	 * 
	 * @param directoryUser
	 *            , diretório a ser verificado para exclusão
	 */
	public static void checkDiretoryUser(String directoryUser) {
		if (directoryUser != null) {
			File dir = new File(directoryUser);
			try {
				FileUtils.forceDelete(dir);
			} catch (IOException e) {
				new ExceptionManager(e).treatException();
			}
		}
	}

	/**
	 * Método para mover um arquivo de um diretório para outro
	 * 
	 * @param srcPath
	 *            , Caminho da origem do arquivo
	 * @param dstPath
	 *            , Caminho para onde vai o arquivo
	 * @return
	 */
	public static boolean moveFile(String srcPath, String dstPath) {
		InputStream in;
		OutputStream out;
		if (new File(srcPath).exists()) {
			try {
				in = new FileInputStream(srcPath);
				out = new FileOutputStream(dstPath);
				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
				deleteFileFolder(null, srcPath);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else
			return false;
	}
}

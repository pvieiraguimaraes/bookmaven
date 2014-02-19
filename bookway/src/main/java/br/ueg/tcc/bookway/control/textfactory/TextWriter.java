package br.ueg.tcc.bookway.control.textfactory;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import br.com.vexillum.control.manager.ExceptionManager;
import br.com.vexillum.util.Return;
import br.ueg.tcc.bookway.model.Text;
import br.ueg.tcc.bookway.model.UserBookway;
import br.ueg.tcc.bookway.utils.FolderUtils;

/**
 * Classe respons�vel pela intera��o que o arquivo do texto realiza��o na
 * escrita seja na inser��o ou na exclus�o de arquivo, onde est�o presentes os
 * m�todos respons�veis pelas intera��es necess�rias para manuten��o dos
 * reposit�rios dos arquivos de texto.
 * 
 * @author pedro
 * 
 */
@SuppressWarnings("serial")
public class TextWriter extends TextBookwayIO {

	public TextWriter(HashMap<String, Object> map) {
		textData = map;
	}

	/**
	 * M�todo que insere o arquivo do texto em no reposit�rio do usu�rio e caso
	 * n�o exista tal reposit�rio o mesmo � criado.
	 * 
	 * @return {@link Return}
	 */
	public Return insertTextIntoRepository() {
		Return retTxtInsert = new Return(true);
		try {
			XMLOutputter outputter = new XMLOutputter();
			outputter.setFormat(Format.getPrettyFormat().setOmitEncoding(true)
					.setOmitDeclaration(true));
			String separator = System.getProperty("file.separator");
			String nameUser = ((UserBookway) textData.get("user")).getEmail();
			Document doc = (Document) textData.get("document");
			Text text = (Text) textData.get("text");
			FolderUtils.createFolder(
					configuration.getKey("PATH_REPOSITORY_TEXT"), nameUser);
			String fileName = configuration.getKey("PATH_REPOSITORY_TEXT")
					+ separator + nameUser + separator + text.getTitle().trim()
					+ new Date().getTime() + ".xml";
			doc.setDocType(null);
			FileWriter writter = new FileWriter(fileName);
			outputter.output(doc, writter);
			writter.close();
			List<String> list = new ArrayList<String>();
			list.add(fileName);
			retTxtInsert.setList(list);
		} catch (Exception e) {
			retTxtInsert.setValid(false);
			new ExceptionManager(e).treatException();
		}
		return retTxtInsert;
	}

	/**
	 * Remove um determinado arquivo do reposit�rio do usu�rio
	 * 
	 * @param filePath
	 *            , caminho completo do arquivo a ser deletado do reposit�rio
	 * @return {@link Return}
	 */
	public Return removeTextFromRepository(String filePath) {
		Return ret = new Return(true);
		String nameUser = ((UserBookway) textData.get("user")).getEmail();
		String separator = System.getProperty("file.separator");
		String path = configuration.getKey("PATH_REPOSITORY_TEXT");
		String directoryUser = path + separator + nameUser;
		ret.setValid(FolderUtils.deleteFileFolder(directoryUser, filePath));
		return ret;
	}

	/**
	 * M�todo para mover os arquivos dentro do reposit�rio do usu�rio, para
	 * mover para o ou do reposit�rio p�blico para colocar como destino ou
	 * caminho "public"
	 * 
	 * @param srcPath
	 * @param dstPath
	 * @return
	 */
	public Return moveFile(String srcPath, String dstPath) {
		Return ret = new Return(true);
		if (dstPath.equalsIgnoreCase("public"))
			dstPath = configuration.getKey("PATH_PUBLIC_REPOSITORY_TEXT");
		ret.setValid(FolderUtils.moveFile(srcPath, dstPath));
		return ret;
	}

}

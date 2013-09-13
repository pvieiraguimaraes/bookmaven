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

@SuppressWarnings("serial")
public class TextWriter extends TextBookwayIO {

	public TextWriter(HashMap<String, Object> map) {
		textData = map;
	}

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
			outputter.output(doc, new FileWriter(fileName));
			List<String> list = new ArrayList<String>();
			list.add(fileName);
			retTxtInsert.setList(list);
		} catch (Exception e) {
			retTxtInsert.setValid(false);
			new ExceptionManager(e).treatException();
		}
		return retTxtInsert;
	}

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
	 * Método para mover os arquivos dentro do repositório do usuário, para
	 * mover para o ou do repositório público para colocar como destino ou
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
		if (srcPath.equalsIgnoreCase("public"))
			srcPath = configuration.getKey("PATH_PUBLIC_REPOSITORY_TEXT");
		ret.setValid(FolderUtils.moveFile(srcPath, dstPath));
		return ret;
	}

}

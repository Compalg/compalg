package Editor.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ExProperties {

	public static void main(String[] args) {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		File file = new File ("editor.bila");
		Properties props = new Properties();
		try {
			fis = new FileInputStream(file);
			props.load(fis);
			fis.close();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
                
               //imprime o conteudo do objeto na consola
		props.list(System.out);		
		
		try {
			fos = new FileOutputStream(file);
			props.store(fos, "Configuração do editor");
			fos.close();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
}

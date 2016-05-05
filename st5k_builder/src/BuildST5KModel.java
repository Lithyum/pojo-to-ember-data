import com.worldline.ember.converter.Config;
import com.worldline.ember.converter.POJO2EmberData;

/** Created by: rja (2014/11/22 19:51) */


public class BuildST5KModel {

	public static void main(String[] args) {
		String[] packagesToScan = new String[] { "pt.opensoft.st5k.model" };
		String emberModelNamespace = "ST5KApp";
		Config config = new Config(packagesToScan, emberModelNamespace, "C:\\projects\\My Projects\\SpringT5K\\src\\main\\webapp\\js\\model\\");
		POJO2EmberData converter = new POJO2EmberData(config);
		converter.convert(); // Your models will be generated in the generated_models dir
	}
}

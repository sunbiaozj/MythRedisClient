package redis.manager;

import com.redis.SpringInit;
import com.redis.config.PoolManagement;
import com.redis.config.RedisPools;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import redis.clients.jedis.Jedis;
import redis.manager.controller.ConnectController;
import redis.manager.controller.MainController;

import java.io.IOException;


/**
 * 应用主程序.
 *
 */
public class Main extends Application {


    private AnchorPane rootLayout;
    private FXMLLoader rootLoader;
    private Stage primaryStage;



    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Redis客户端");

        rootLoader = new FXMLLoader();
        rootLoader.setLocation(this.getClass().getResource("/views/MainLayout.fxml"));
        rootLayout = rootLoader.load();

        Scene scene = new Scene(rootLayout);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();

        MainController mainController = rootLoader.getController();
        mainController.setMain(this);
    }


    public boolean showConnectPanel() {
        boolean ok = false;

        // 创建 FXMLLoader 对象
        FXMLLoader loader = new FXMLLoader();
        // 加载文件
        loader.setLocation(this.getClass().getResource("/views/ConnectLayout.fxml"));
        AnchorPane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 创建对话框
        Stage dialogStage = new Stage();
        dialogStage.setTitle("创建连接");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(pane);
        dialogStage.setScene(scene);

        ConnectController connectController = loader.getController();
        connectController.setDialogStage(dialogStage);

        // 显示对话框, 并等待, 直到用户关闭
        dialogStage.showAndWait();

        return connectController.isOkChecked();
    }


    public static void main( String[] args ) {
        ApplicationContext context;
        PoolManagement management;
        context = new AnnotationConfigApplicationContext(SpringInit.class);
        management = (PoolManagement)context.getBean("poolManagement");
        management.setCurrentPoolId("1001");
        /*RedisPools pools = management.getRedisPool();
        Jedis jedis = pools.getJedis();
        jedis.set("name","huang");
        System.out.println(jedis.get("name"));*/
        launch(args);
    }
}

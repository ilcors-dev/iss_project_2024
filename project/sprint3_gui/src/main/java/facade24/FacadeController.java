package facade24;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import unibo.basicomm23.utils.CommUtils;
 

 

@Controller
public class FacadeController {
 
    @Value("${spring.application.name}")
    String appNameOld;  //vedi application.properties
     
    protected String mainPage = "WebRobot24Gui"; //TODO: "WebRobot24Gui";  

    public FacadeController(){
        CommUtils.outgreen (" --- FacadeController | STARTS " );
        new FacadeBuilder( ) ;
    }
 

    protected String buildThePage(Model viewmodel) {
        setConfigParams(viewmodel);
        return mainPage;
    }
    protected void setConfigParams(Model viewmodel){
 
    }
    @GetMapping("/")
    public String homePage(Model viewmodel) {
        //CommUtils.outcyan("FacadeController homePage appNameOld=" + appNameOld);
        viewmodel.addAttribute("appname", ApplSystemInfo.appName);
        String dir = System.getProperty("user.dir");
        CommUtils.outgreen (" --- FacadeController | entry dir= "+dir  );
        return buildThePage(viewmodel); //"qakFacadeGUI";
    }
    
}

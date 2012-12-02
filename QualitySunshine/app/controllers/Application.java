package controllers;

import aalto.media.newsml.PackageGenerator;
import aalto.media.newsml.PackageItem;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
  public static Result index() {
    PackageGenerator pg = new PackageGenerator("public/pictureitems");
    PackageItem pi = pg.generatePackage();
    return ok(index.render("Your new application is ready." +  pi.newsItems.get(0).toString()));
  }
  
}
package greycat.samples.gps;

/**
 * Created by Quentin on 09/06/2017.
 */
public class Vehicle_GPS {

    String name;
    String type;
    Double longitude;
    Double latitude;

    //Constructeur par défaut
    public Vehicle_GPS(){

        name = "Unknow";
        type = "Unknow";
        longitude = 0.0;
        latitude = 0.0;
    }

    public Vehicle_GPS(String pName, String pType, Double pLongitude, Double pLatitude)
    {
        name = pName;
        type = pType;
        longitude = pLongitude;
        latitude = pLatitude;
    }
}


package com.example.user.notendings;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by rijad on 01.04.16.
 */
public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    public static void main(String[] args) throws SQLException, IOException {
        writeConfigFile("ormlite_config.txt");
    }
}

package com.jredu.com.util;

/**
 * Created by Administrator on 2015/9/29 0029.
 */
public class SysContants {

    public static final class BlackTable{

        public static final String BLACK_TABLE_NAME="black_list";

        public static final String UESR_NAME="name";

        public static final String USER_NUMBER="number";

        public static String getCreateSQL(){
            StringBuffer sb = new StringBuffer();
            sb.append(" CREATE TABLE IF NOT EXISTS  ");
            sb.append(BLACK_TABLE_NAME );
            sb.append(" ( ");

            sb.append(UESR_NAME);
            sb.append(" varchar(20)  null ,");
            sb.append(USER_NUMBER);
            sb.append(" varchar(20) ");
            sb.append(")");


            return sb.toString();
        }





    }
}

package org.ex2_3.utils;


import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static org.ex2_3.utils.LogUtil.logException;

public class HibernateUtil {

    private static SessionFactory factory;

    static {
        try {
            factory = new Configuration()
                    .configure()
                    .buildSessionFactory();
        } catch (HibernateException e) {
            logException(e, HibernateUtil.class.getName() );
        }
    }
    public static SessionFactory getSessionFactory() {
        return factory;
    }

}

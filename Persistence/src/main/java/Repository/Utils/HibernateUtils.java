package Repository.Utils;

import Domain.Angajat;
import Domain.Spectacol;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

@Component
public class HibernateUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if ((sessionFactory == null) || (sessionFactory.isClosed()))
            sessionFactory = createNewSessionFactory();
        return sessionFactory;
    }

    private static SessionFactory createNewSessionFactory(){
        sessionFactory = new Configuration()
                .addAnnotatedClass(Angajat.class)
                .addAnnotatedClass(Spectacol.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static void closeSessionFactory(){
        if (sessionFactory != null)
            sessionFactory.close();
    }
}
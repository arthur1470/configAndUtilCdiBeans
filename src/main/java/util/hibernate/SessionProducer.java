package br.com.teste.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class SessionProducer {

    private SessionFactory sessionFactory;

    public SessionProducer() {
        Configuration conf = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(conf.getProperties());
        sessionFactory = conf.buildSessionFactory(builder.build());
    }
        
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Produces
    @RequestScoped
    public Session getSession() {
        return getSessionFactory().openSession();
    }

    public void closeSession(@Disposes Session session) {
        session.close();
    }
}

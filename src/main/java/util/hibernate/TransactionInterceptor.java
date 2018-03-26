package br.com.teste.hibernate;

import org.hibernate.Session;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@Interceptor
@Transactional
public class TransactionInterceptor {

    @Inject
    private Session session;

    @AroundInvoke
    public Object invoke(InvocationContext context) throws Exception {
        boolean criador = false;
        try {
            if (!session.getTransaction().isActive()) {
                // truque para fazer um rollback no que já passsou
                // (senão, um futuro commit, confirmaria até mesmo operações sem transação)
                session.getTransaction().begin();
                session.getTransaction().rollback();
                // agora sim inicia a transação
                session.getTransaction().begin();
                criador = true;
            }
            return context.proceed();
        } catch (Exception e) {
            if (session.getTransaction() != null && criador) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (session.getTransaction() != null && session.getTransaction().isActive() && criador) {
                session.getTransaction().commit();
            }
        }
    }
}

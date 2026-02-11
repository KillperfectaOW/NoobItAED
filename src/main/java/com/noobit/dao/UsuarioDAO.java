package com.noobit.dao;

import com.noobit.model.Recompensa;
import com.noobit.model.Usuario;
import javax.persistence.*;
import java.util.List;

public class UsuarioDAO {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("NoobitPU");

    // --- GUARDAR (Sirve para Usuario y Recompensa) ---
    public void guardar(Object entidad) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entidad);
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace(); // Muestra error en consola
            throw e;             // <--- ¡IMPORTANTE! Lanza el error hacia arriba
        } finally {
            em.close();
        }
    }

    // --- LOGIN ---
    public Usuario login(String user, String pass) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.username = :u AND u.password = :p", Usuario.class)
                    .setParameter("u", user)
                    .setParameter("p", pass)
                    .getSingleResult();
        } catch (Exception e) { return null; }
        finally { em.close(); }
    }

    // --- PARA EL ADMIN ---

    // Lista de todos los usuarios
    public List<Usuario> listarUsuarios() {
        EntityManager em = emf.createEntityManager();
        List<Usuario> lista = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        em.close();
        return lista;
    }

    // Buscar usuario por ID (para editar puntos)
    public Usuario buscarPorId(int id) {
        EntityManager em = emf.createEntityManager();
        Usuario u = em.find(Usuario.class, id);
        em.close();
        return u;
    }
    // --- NUEVO: OBTENER LISTA DE LA TIENDA ---
    public java.util.List<com.noobit.model.Recompensa> obtenerRecompensas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT r FROM Recompensa r", com.noobit.model.Recompensa.class).getResultList();
        } finally {
            em.close();
        }
    }
    public void eliminarUsuario(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Buscamos al usuario por ID
            Usuario u = em.find(Usuario.class, id);
            if (u != null) {
                em.remove(u); // Lo borramos
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if(em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    //  OBTENER INVENTARIO DE UN USUARIO
    public List<Recompensa> obtenerInventarioUsuario(int idUsuario) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT r FROM Recompensa r JOIN InventarioUsuario i " +
                                    "ON r.id = i.idRecompensa WHERE i.idUsuario = :id",
                            Recompensa.class)
                    .setParameter("id", idUsuario)
                    .getResultList();
        } finally {
            em.close();
        }
    }

}
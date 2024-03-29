package session;

import entity.Event;
import entity.User;
import error.NoResultException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Admin
 */
@Stateless
public class EventController implements EventControllerLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    //  private UserControllerLocal userControllerLocal;

    @Override
    public Event createEvent(Event newEvent) {
        em.persist(newEvent);
        em.flush();

        System.out.println("Create new Event > " + newEvent.getId());
        return newEvent;
    }

    @Override
    public Event updateEvent(Event currEvent) throws NoResultException {
        Event oldEvent = em.find(Event.class, currEvent.getId());
        try {
            oldEvent.setTitle(currEvent.getTitle());
            oldEvent.setStartDate(currEvent.getStartDate());
            oldEvent.setEndDate(currEvent.getEndDate());

            System.out.println("Event updated");
            return currEvent;

        } catch (Exception ex) {
            throw new NoResultException("Failed to Update Event");
        }
    }

    @Override
    public List<Event> retrieveAllEvents() throws NoResultException {
        Query query = em.createQuery("SELECT e FROM Event e WHERE e.id = :inEventId");
        query.setParameter("inEventId", Event.class);

        return query.getResultList();
    }

    @Override
    public List<Event> retrieveAllAvailableEventForUser(User currUser) {
        Query query = em.createQuery("SELECT e FROM User u, IN(u.events)e WHERE u.id =:inUserId");
        query.setParameter("inUserId", currUser.getId());
        // current user is set to null
        List<Event> eventList = query.getResultList();

        return eventList;
    }

    @Override
    public void deleteEvent(Long eventId) throws NoResultException {
        try {
            Query query = em.createQuery("SELECT e FROM Event e WHERE e.Id = :id");
            query.setParameter("id", eventId);

            Event delEvent = (Event) query.getSingleResult();

            em.remove(delEvent);
        } catch (Exception ex) {
            throw new NoResultException("Event " + eventId + "does not exist!");
        }
    }

    @Override
    public Event findEventById(Long id) {
        return em.find(Event.class, id);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}

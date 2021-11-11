package at.roeblfruechte.control;

import at.roeblfruechte.model.Absence;
import at.roeblfruechte.model.Day;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class DayRepository implements PanacheRepositoryBase<Day, Long> {

    public Day findDayById(Long id){
        Optional<Day> optionalDay = Day.findByIdOptional(id);
        return optionalDay.orElseThrow(NotFoundException::new);
    }

    public Day persistDay(Day day){return this.getEntityManager().merge(day);}

    public Day updateDay(Long id, Day day){
        Day updateDay = findDayById(id);
        updateDay.CopyProperties(day);
        return persistDay(updateDay);
    }

    public void deleteDay(Long id){ delete(findDayById(id));}
}

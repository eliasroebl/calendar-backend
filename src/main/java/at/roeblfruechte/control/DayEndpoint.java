package at.roeblfruechte.control;

import at.roeblfruechte.boundary.DayRepository;
import at.roeblfruechte.model.Day;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/api/day")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class DayEndpoint {
    
    @Inject
    DayRepository dayRepository;

    @GET
    @Path("{id}")
    public Day getDayById(@PathParam("id")Long id){
        return dayRepository.findDayById(id);
    }

    @POST
    public Response createDay(@Context UriInfo info, Day day){
        if(day == null) return Response.noContent().build();
        Day newDay = new Day();
        dayRepository.persist(newDay);
        //If 1-* Relationship, persist here
        newDay.CopyProperties(day);
        Day savedDay = dayRepository.persistDay(newDay);
        URI uri = info.getAbsolutePathBuilder().path("/" + savedDay.id).build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteDay(@PathParam("id") Long id) {
        try {
            dayRepository.deleteDay(id);
        } catch (EntityNotFoundException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .header("Reason", "Day with id " + id + " does not exist")
                    .build();
        }
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") Long id, Day day) {
        Day dayToUpdate = dayRepository.updateDay(id, day);
        if (dayToUpdate == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .header("Reason", "Day with id " + id + " does not exist")
                    .build();
        } else {
            dayRepository.updateDay(id,day);
            return Response.ok().build();
        }
    }
}

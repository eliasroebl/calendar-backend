package at.roeblfruechte.boundary;

import at.roeblfruechte.control.AbsenceRepository;
import at.roeblfruechte.model.Absence;

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
import java.util.List;

@Path("/api/absence")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class AbsenceEndpoint {

    @Inject
    AbsenceRepository absenceRepository;

    @GET
    public List<Absence> getAllAbsences(){
        return absenceRepository.findAll().list();
    }

    @GET
    @Path("{id}")
    public Absence getAbsenceById(@PathParam("id")Long id){
        return absenceRepository.findAbsenceById(id);
    }

    @GET
    @Path("{employee}")
    public List<Absence> getAbsencesByEmployee(@PathParam("employee")Long employeeId)
    {
        return absenceRepository.findAbsencesByEmployee(employeeId);
    }

    @POST
    public Response createAbsence(@Context UriInfo info, Absence absence){
        if(absence == null) return Response.noContent().build();
        Absence newAbsence = new Absence();
        absenceRepository.persist(newAbsence);
        //If 1-* Relationship, persist here
        newAbsence.CopyProperties(absence);
        Absence savedAbsence = absenceRepository.persistAbsence(newAbsence);
        URI uri = info.getAbsolutePathBuilder().path("/" + savedAbsence.id).build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAbsence(@PathParam("id") Long id) {
        try {
            absenceRepository.deleteAbsence(id);
        } catch (EntityNotFoundException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .header("Reason", "Absence with id " + id + " does not exist")
                    .build();
        }
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") Long id, Absence absence) {
        Absence absenceToUpdate = absenceRepository.findAbsenceById(id);
        if (absenceToUpdate == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .header("Reason", "Absence with id " + id + " does not exist")
                    .build();
        } else {
            absenceRepository.updateAbsence(id,absence);
            return Response.accepted().build();
        }
    }
}

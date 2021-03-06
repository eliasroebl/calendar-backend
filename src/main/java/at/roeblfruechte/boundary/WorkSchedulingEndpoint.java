package at.roeblfruechte.boundary;

import at.roeblfruechte.control.EmployeeRepository;
import at.roeblfruechte.control.WorkSchedulingRepository;
import at.roeblfruechte.model.Employee;
import at.roeblfruechte.model.WorkScheduling;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("/api/workScheduling")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class WorkSchedulingEndpoint {

    @Inject
    WorkSchedulingRepository workSchedulingRepository;

    @Inject
    EmployeeRepository employeeRepository;

    @GET
    public List<WorkScheduling> getWorkSchedulings(){
        return workSchedulingRepository.findAllWorkSchedulings();
    }

    @GET
    @Path("{id}")
    public WorkScheduling getWorkSchedulingById(@PathParam("id")Long id){
        return workSchedulingRepository.findWorkSchedulingById(id);
    }

    @GET
    @Path("date/{date}")
    public List<WorkScheduling> getCurrentWeekWorkScheduling(@PathParam("date") String date){
        LocalDate ld = LocalDate.parse(date);
        return workSchedulingRepository.findCurrentWeekWorkSchedulings(ld);
    }

    @POST
    public Response createWorkScheduling(@Context UriInfo info, WorkScheduling workScheduling){
        if(workScheduling == null) return Response.noContent().build();
        workScheduling.employee = employeeRepository.findEmployeeById(workScheduling.employee.id);
        WorkScheduling newWorkScheduling = new WorkScheduling();
        //workSchedulingRepository.persist(newWorkScheduling);
        newWorkScheduling.CopyProperties(workScheduling);
        WorkScheduling savedWorkScheduling = workSchedulingRepository.persistWorkScheduling(newWorkScheduling);
        URI uri = info.getAbsolutePathBuilder().path("/" + savedWorkScheduling.id).build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteWorkScheduling(@PathParam("id") Long id) {
        try {
            workSchedulingRepository.deleteWorkScheduling(id);
        } catch (EntityNotFoundException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .header("Reason", "WorkScheduling with id " + id + " does not exist")
                    .build();
        }
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") Long id, WorkScheduling workScheduling) {
        WorkScheduling workSchedulingToUpdate = workSchedulingRepository.findWorkSchedulingById(id);
        if (workSchedulingToUpdate == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .header("Reason", "WorkScheduling with id " + id + " does not exist")
                    .build();
        } else {
            workSchedulingRepository.updateWorkScheduling(id,workScheduling);
            return Response.ok().build();
        }
    }
}

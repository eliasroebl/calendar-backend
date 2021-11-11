package at.roeblfruechte.control;

import at.roeblfruechte.boundary.WorkScheduleRepository;
import at.roeblfruechte.boundary.WorkSchedulingRepository;
import at.roeblfruechte.model.WorkSchedule;
import at.roeblfruechte.model.WorkScheduling;

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
import java.util.ArrayList;
import java.util.List;

@Path("/api/workSchedule")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
public class WorkScheduleEndpoint {

    @Inject
    WorkScheduleRepository workScheduleRepository;

    @Inject
    WorkSchedulingRepository workSchedulingRepository;

    @GET
    public List<WorkSchedule> getWorkSchedules(){
        return workScheduleRepository.findAllWorkSchedules();
    }

    @GET
    @Path("{id}")
    public WorkSchedule getWorkScheduleById(@PathParam("id")Long id){
        return workScheduleRepository.findWorkScheduleById(id);
    }

    @POST
    public Response createWorkSchedule(@Context UriInfo info, WorkSchedule workSchedule){
        if(workSchedule == null) return Response.noContent().build();
        WorkSchedule newWorkSchedule = new WorkSchedule();
        //workScheduleRepository.persistWorkSchedule(newWorkSchedule); <- WTF IS THIS
        if(workSchedule.workSchedulingList == null){
            workSchedule.workSchedulingList = new ArrayList<>();
        }
        workSchedule.workSchedulingList.forEach(s -> {
            WorkScheduling workScheduling = new WorkScheduling();
            workScheduling.CopyProperties(s);
            workScheduling.employee = s.employee;
            workScheduling.workSchedule = s.workSchedule;
            workScheduling.location = s.location;
            workScheduling.from = s.from;
            workScheduling.to = s.to;
            workScheduling = workSchedulingRepository.persistWorkScheduling(workScheduling);
            newWorkSchedule.workSchedulingList.add(workScheduling);
        });
        newWorkSchedule.CopyProperties(workSchedule);
        WorkSchedule savedWorkSchedule = workScheduleRepository.persistWorkSchedule(newWorkSchedule);
        URI uri = info.getAbsolutePathBuilder().path("/" + savedWorkSchedule.id).build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteWorkSchedule(@PathParam("id") Long id) {
        try {
            workScheduleRepository.deleteWorkSchedule(id);
        } catch (EntityNotFoundException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .header("Reason", "WorkSchedule with id " + id + " does not exist")
                    .build();
        }
        return Response.noContent().build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response put(@PathParam("id") Long id, WorkSchedule workSchedule) {
        WorkSchedule workScheduleToUpdate = workScheduleRepository.findWorkScheduleById(id);
        if (workScheduleToUpdate == null) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .header("Reason", "WorkSchedule with id " + id + " does not exist")
                    .build();
        } else {
            workScheduleRepository.updateWorkSchedule(id,workSchedule);
            return Response.accepted().build();
        }
    }
}

package se.group.backendgruppuppgift.tasker.resource;

import org.springframework.stereotype.Component;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.TaskStatus;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.service.TaskService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

@Component
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("tasks")
public final class TaskResource {

    @Context
    private UriInfo uriInfo;

    private final TaskService service;

    public TaskResource(TaskService service) {
        this.service = service;
    }

    @POST
    public Response createTask(TaskWeb task) {
        Task result = service.createTask(task);
        return Response.created(URI.create(uriInfo
                .getAbsolutePathBuilder()
                .path(result.getId().toString())
                .toString()))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateTask(@PathParam("id") Long id, TaskWeb task) {
        return service.updateTask(id, task)
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @GET
    @Path("{id}")
    public Response findTask(@PathParam("id") Long id) {
        return service.findTask(id)
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND))
                .build();
    }



    @GET
    public Response findTaskByParameter(@QueryParam("status") @DefaultValue("") String status, @QueryParam("text") @DefaultValue("") String text){
        System.out.println("status = "+status);
        System.out.println("text = "+text);
        if(!status.isEmpty()){
            return Response.ok(service.findTaskByStatus(status)).build();
        }

        else if(!text.isEmpty()){
            return Response.ok(service.findTaskByText(text)).build();
        }

        return Response.status(NOT_FOUND).build();

    }
    /*@DELETE
    @Path("{id}")
    public void deleteTaskById(@PathParam("id") Long id){
        service.deleteTaskById(id);
    }*/


}

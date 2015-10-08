package com.evanzeimet.queryinfo.it.people;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.evanzeimet.queryinfo.QueryInfo;

@Path("people")
public interface PeopleResource {

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	Response query(QueryInfo queryInfo);

}

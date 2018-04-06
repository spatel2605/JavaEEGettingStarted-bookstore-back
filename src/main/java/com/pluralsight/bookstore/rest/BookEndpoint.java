package com.pluralsight.bookstore.rest;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.repository.BookRepository;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/books")
public class BookEndpoint {

    @Inject
    private BookRepository bookRepository;

    @GET
    @Path("/{id : \\d+}")
    @Produces(APPLICATION_JSON)
    public Response getBook(@PathParam("id") @Min(1) Long id) {
        Book book = bookRepository.find(id);

        if (book == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(book).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getBooks() {
        List<Book> books = bookRepository.findAll();

        if (books.size() == 0)
            return Response.noContent().build();

        return Response.ok(books).build();
    }

    @GET
    @Path("/count")
    public Response countBooks() {
        Long noOfBooks = bookRepository.countAll();

        if(noOfBooks == 0)
            return Response.noContent().build();

        return Response.ok(noOfBooks).build();
    }

    @GET
    @Consumes(APPLICATION_JSON)
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        book = bookRepository.create(book);
        URI createdUri = uriInfo.getBaseUriBuilder().path(book.getId().toString()).build();
        return Response.created(createdUri).build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    public Response deleteBook(@PathParam("id") @Min(1) Long id) {
        bookRepository.delete(id);
        return Response.noContent().build();
    }
}

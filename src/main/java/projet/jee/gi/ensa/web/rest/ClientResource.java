package projet.jee.gi.ensa.web.rest;

import com.codahale.metrics.annotation.Timed;
import projet.jee.gi.ensa.domain.Client;
import projet.jee.gi.ensa.repository.ClientRepository;
import projet.jee.gi.ensa.web.rest.errors.BadRequestAlertException;
import projet.jee.gi.ensa.web.rest.util.HeaderUtil;
import projet.jee.gi.ensa.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Client.
 */
@RestController
@RequestMapping("/api")
public class ClientResource {

    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private static final String ENTITY_NAME = "client";

    private final ClientRepository clientRepository;

    public ClientResource(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * POST  /clients : Create a new client.
     *
     * @param client the client to create
     * @return the ResponseEntity with status 201 (Created) and with body the new client, or with status 400 (Bad Request) if the client has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/clients")
    @Timed
    public ResponseEntity<Client> createClient(@RequestBody Client client) throws URISyntaxException {
        log.debug("REST request to save Client : {}", client);
        if (client.getId() != null) {
            throw new BadRequestAlertException("A new client cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Client result = clientRepository.save(client);
        return ResponseEntity.created(new URI("/api/clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /clients : Updates an existing client.
     *
     * @param client the client to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated client,
     * or with status 400 (Bad Request) if the client is not valid,
     * or with status 500 (Internal Server Error) if the client couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/clients")
    @Timed
    public ResponseEntity<Client> updateClient(@RequestBody Client client) throws URISyntaxException {
        log.debug("REST request to update Client : {}", client);
        if (client.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Client result = clientRepository.save(client);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, client.getId().toString()))
            .body(result);
    }

    /**
     * GET  /clients : get all the clients.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of clients in body
     */
    @GetMapping("/clients")
    @Timed
    public ResponseEntity<List<Client>> getAllClients(Pageable pageable) {
        log.debug("REST request to get a page of Clients");
        Page<Client> page = clientRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clients");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /clients/:id : get the "id" client.
     *
     * @param id the id of the client to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the client, or with status 404 (Not Found)
     */
    @GetMapping("/clients/{id}")
    @Timed
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        Optional<Client> client = clientRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(client);
    }

    /**
     * DELETE  /clients/:id : delete the "id" client.
     *
     * @param id the id of the client to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/clients/{id}")
    @Timed
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.debug("REST request to delete Client : {}", id);

        clientRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

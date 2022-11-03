package com.pe.pandero.asamblea.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.pe.pandero.asamblea.IntegrationTest;
import com.pe.pandero.asamblea.domain.Balotas;
import com.pe.pandero.asamblea.repository.BalotasRepository;
import com.pe.pandero.asamblea.service.EntityManager;
import com.pe.pandero.asamblea.service.dto.BalotasDTO;
import com.pe.pandero.asamblea.service.mapper.BalotasMapper;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link BalotasResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class BalotasResourceIT {

    private static final Float DEFAULT_NUMERO = 1F;
    private static final Float UPDATED_NUMERO = 2F;

    private static final Instant DEFAULT_CREADO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREADO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/balotas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private BalotasRepository balotasRepository;

    @Autowired
    private BalotasMapper balotasMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Balotas balotas;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Balotas createEntity(EntityManager em) {
        Balotas balotas = new Balotas().numero(DEFAULT_NUMERO).creado(DEFAULT_CREADO);
        return balotas;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Balotas createUpdatedEntity(EntityManager em) {
        Balotas balotas = new Balotas().numero(UPDATED_NUMERO).creado(UPDATED_CREADO);
        return balotas;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Balotas.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        balotas = createEntity(em);
    }

    @Test
    void createBalotas() throws Exception {
        int databaseSizeBeforeCreate = balotasRepository.findAll().collectList().block().size();
        // Create the Balotas
        BalotasDTO balotasDTO = balotasMapper.toDto(balotas);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(balotasDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Balotas in the database
        List<Balotas> balotasList = balotasRepository.findAll().collectList().block();
        assertThat(balotasList).hasSize(databaseSizeBeforeCreate + 1);
        Balotas testBalotas = balotasList.get(balotasList.size() - 1);
        assertThat(testBalotas.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testBalotas.getCreado()).isEqualTo(DEFAULT_CREADO);
    }

    @Test
    void createBalotasWithExistingId() throws Exception {
        // Create the Balotas with an existing ID
        balotas.setId(1L);
        BalotasDTO balotasDTO = balotasMapper.toDto(balotas);

        int databaseSizeBeforeCreate = Objects.requireNonNull(balotasRepository.findAll().collectList().block()).size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(balotasDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Balotas in the database
        List<Balotas> balotasList = balotasRepository.findAll().collectList().block();
        assertThat(balotasList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllBalotasAsStream() {
        // Initialize the database
        balotasRepository.save(balotas).block();

        List<Balotas> balotasList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(BalotasDTO.class)
            .getResponseBody()
            .map(balotasMapper::toEntity)
            .filter(balotas::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(balotasList).isNotNull();
        assertThat(balotasList).hasSize(1);
        Balotas testBalotas = balotasList.get(0);
        assertThat(testBalotas.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testBalotas.getCreado()).isEqualTo(DEFAULT_CREADO);
    }

    @Test
    void getAllBalotas() {
        // Initialize the database
        balotasRepository.save(balotas).block();

        // Get all the balotasList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(balotas.getId().intValue()))
            .jsonPath("$.[*].numero")
            .value(hasItem(DEFAULT_NUMERO.doubleValue()))
            .jsonPath("$.[*].creado")
            .value(hasItem(DEFAULT_CREADO.toString()));
    }

    @Test
    void getBalotas() {
        // Initialize the database
        balotasRepository.save(balotas).block();

        // Get the balotas
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, balotas.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(balotas.getId().intValue()))
            .jsonPath("$.numero")
            .value(is(DEFAULT_NUMERO.doubleValue()))
            .jsonPath("$.creado")
            .value(is(DEFAULT_CREADO.toString()));
    }

    @Test
    void getNonExistingBalotas() {
        // Get the balotas
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewBalotas() throws Exception {
        // Initialize the database
        balotasRepository.save(balotas).block();

        int databaseSizeBeforeUpdate = Objects.requireNonNull(balotasRepository.findAll().collectList().block()).size();

        // Update the balotas
        Balotas updatedBalotas = balotasRepository.findById(balotas.getId()).block();
        assert updatedBalotas != null;
        updatedBalotas.numero(UPDATED_NUMERO).creado(UPDATED_CREADO);
        BalotasDTO balotasDTO = balotasMapper.toDto(updatedBalotas);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, balotasDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(balotasDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Balotas in the database
        List<Balotas> balotasList = balotasRepository.findAll().collectList().block();
        assertThat(balotasList).hasSize(databaseSizeBeforeUpdate);
        assert balotasList != null;
        Balotas testBalotas = balotasList.get(balotasList.size() - 1);
        assertThat(testBalotas.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testBalotas.getCreado()).isEqualTo(UPDATED_CREADO);
    }

    @Test
    void putNonExistingBalotas() throws Exception {
        int databaseSizeBeforeUpdate = Objects.requireNonNull(balotasRepository.findAll().collectList().block()).size();
        balotas.setId(count.incrementAndGet());

        // Create the Balotas
        BalotasDTO balotasDTO = balotasMapper.toDto(balotas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, balotasDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(balotasDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Balotas in the database
        List<Balotas> balotasList = balotasRepository.findAll().collectList().block();
        assertThat(balotasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchBalotas() throws Exception {
        int databaseSizeBeforeUpdate = balotasRepository.findAll().collectList().block().size();
        balotas.setId(count.incrementAndGet());

        // Create the Balotas
        BalotasDTO balotasDTO = balotasMapper.toDto(balotas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(balotasDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Balotas in the database
        List<Balotas> balotasList = balotasRepository.findAll().collectList().block();
        assertThat(balotasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamBalotas() throws Exception {
        int databaseSizeBeforeUpdate = balotasRepository.findAll().collectList().block().size();
        balotas.setId(count.incrementAndGet());

        // Create the Balotas
        BalotasDTO balotasDTO = balotasMapper.toDto(balotas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(balotasDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Balotas in the database
        List<Balotas> balotasList = balotasRepository.findAll().collectList().block();
        assertThat(balotasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateBalotasWithPatch() throws Exception {
        // Initialize the database
        balotasRepository.save(balotas).block();

        int databaseSizeBeforeUpdate = balotasRepository.findAll().collectList().block().size();

        // Update the balotas using partial update
        Balotas partialUpdatedBalotas = new Balotas();
        partialUpdatedBalotas.setId(balotas.getId());

        partialUpdatedBalotas.creado(UPDATED_CREADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBalotas.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBalotas))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Balotas in the database
        List<Balotas> balotasList = balotasRepository.findAll().collectList().block();
        assertThat(balotasList).hasSize(databaseSizeBeforeUpdate);
        Balotas testBalotas = balotasList.get(balotasList.size() - 1);
        assertThat(testBalotas.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testBalotas.getCreado()).isEqualTo(UPDATED_CREADO);
    }

    @Test
    void fullUpdateBalotasWithPatch() throws Exception {
        // Initialize the database
        balotasRepository.save(balotas).block();

        int databaseSizeBeforeUpdate = balotasRepository.findAll().collectList().block().size();

        // Update the balotas using partial update
        Balotas partialUpdatedBalotas = new Balotas();
        partialUpdatedBalotas.setId(balotas.getId());

        partialUpdatedBalotas.numero(UPDATED_NUMERO).creado(UPDATED_CREADO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedBalotas.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedBalotas))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Balotas in the database
        List<Balotas> balotasList = balotasRepository.findAll().collectList().block();
        assertThat(balotasList).hasSize(databaseSizeBeforeUpdate);
        Balotas testBalotas = balotasList.get(balotasList.size() - 1);
        assertThat(testBalotas.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testBalotas.getCreado()).isEqualTo(UPDATED_CREADO);
    }

    @Test
    void patchNonExistingBalotas() throws Exception {
        int databaseSizeBeforeUpdate = balotasRepository.findAll().collectList().block().size();
        balotas.setId(count.incrementAndGet());

        // Create the Balotas
        BalotasDTO balotasDTO = balotasMapper.toDto(balotas);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, balotasDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(balotasDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Balotas in the database
        List<Balotas> balotasList = balotasRepository.findAll().collectList().block();
        assertThat(balotasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchBalotas() throws Exception {
        int databaseSizeBeforeUpdate = balotasRepository.findAll().collectList().block().size();
        balotas.setId(count.incrementAndGet());

        // Create the Balotas
        BalotasDTO balotasDTO = balotasMapper.toDto(balotas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(balotasDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Balotas in the database
        List<Balotas> balotasList = balotasRepository.findAll().collectList().block();
        assertThat(balotasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamBalotas() throws Exception {
        int databaseSizeBeforeUpdate = balotasRepository.findAll().collectList().block().size();
        balotas.setId(count.incrementAndGet());

        // Create the Balotas
        BalotasDTO balotasDTO = balotasMapper.toDto(balotas);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(balotasDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Balotas in the database
        List<Balotas> balotasList = balotasRepository.findAll().collectList().block();
        assertThat(balotasList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteBalotas() {
        // Initialize the database
        balotasRepository.save(balotas).block();

        int databaseSizeBeforeDelete = balotasRepository.findAll().collectList().block().size();

        // Delete the balotas
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, balotas.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Balotas> balotasList = balotasRepository.findAll().collectList().block();
        assertThat(balotasList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

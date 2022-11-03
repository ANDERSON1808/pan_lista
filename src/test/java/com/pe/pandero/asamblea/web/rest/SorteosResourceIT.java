package com.pe.pandero.asamblea.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.pe.pandero.asamblea.IntegrationTest;
import com.pe.pandero.asamblea.domain.Sorteos;
import com.pe.pandero.asamblea.repository.SorteosRepository;
import com.pe.pandero.asamblea.service.EntityManager;
import com.pe.pandero.asamblea.service.dto.SorteosDTO;
import com.pe.pandero.asamblea.service.mapper.SorteosMapper;
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
 * Integration tests for the {@link SorteosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class SorteosResourceIT {

    private static final Instant DEFAULT_FECHA_SORTEO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_SORTEO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sorteos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SorteosRepository sorteosRepository;

    @Autowired
    private SorteosMapper sorteosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Sorteos sorteos;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sorteos createEntity(EntityManager em) {
        Sorteos sorteos = new Sorteos().fechaSorteo(DEFAULT_FECHA_SORTEO).codigo(DEFAULT_CODIGO);
        return sorteos;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sorteos createUpdatedEntity(EntityManager em) {
        Sorteos sorteos = new Sorteos().fechaSorteo(UPDATED_FECHA_SORTEO).codigo(UPDATED_CODIGO);
        return sorteos;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Sorteos.class).block();
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
        sorteos = createEntity(em);
    }

    @Test
    void createSorteos() throws Exception {
        int databaseSizeBeforeCreate = sorteosRepository.findAll().collectList().block().size();
        // Create the Sorteos
        SorteosDTO sorteosDTO = sorteosMapper.toDto(sorteos);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sorteosDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Sorteos in the database
        List<Sorteos> sorteosList = sorteosRepository.findAll().collectList().block();
        assertThat(sorteosList).hasSize(databaseSizeBeforeCreate + 1);
        Sorteos testSorteos = sorteosList.get(sorteosList.size() - 1);
        assertThat(testSorteos.getFechaSorteo()).isEqualTo(DEFAULT_FECHA_SORTEO);
        assertThat(testSorteos.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    void createSorteosWithExistingId() throws Exception {
        // Create the Sorteos with an existing ID
        sorteos.setId(1L);
        SorteosDTO sorteosDTO = sorteosMapper.toDto(sorteos);

        int databaseSizeBeforeCreate = sorteosRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sorteosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sorteos in the database
        List<Sorteos> sorteosList = sorteosRepository.findAll().collectList().block();
        assertThat(sorteosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSorteosAsStream() {
        // Initialize the database
        sorteosRepository.save(sorteos).block();

        List<Sorteos> sorteosList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(SorteosDTO.class)
            .getResponseBody()
            .map(sorteosMapper::toEntity)
            .filter(sorteos::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(sorteosList).isNotNull();
        assertThat(sorteosList).hasSize(1);
        Sorteos testSorteos = sorteosList.get(0);
        assertThat(testSorteos.getFechaSorteo()).isEqualTo(DEFAULT_FECHA_SORTEO);
        assertThat(testSorteos.getCodigo()).isEqualTo(DEFAULT_CODIGO);
    }

    @Test
    void getAllSorteos() {
        // Initialize the database
        sorteosRepository.save(sorteos).block();

        // Get all the sorteosList
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
            .value(hasItem(sorteos.getId().intValue()))
            .jsonPath("$.[*].fechaSorteo")
            .value(hasItem(DEFAULT_FECHA_SORTEO.toString()))
            .jsonPath("$.[*].codigo")
            .value(hasItem(DEFAULT_CODIGO));
    }

    @Test
    void getSorteos() {
        // Initialize the database
        sorteosRepository.save(sorteos).block();

        // Get the sorteos
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sorteos.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sorteos.getId().intValue()))
            .jsonPath("$.fechaSorteo")
            .value(is(DEFAULT_FECHA_SORTEO.toString()))
            .jsonPath("$.codigo")
            .value(is(DEFAULT_CODIGO));
    }

    @Test
    void getNonExistingSorteos() {
        // Get the sorteos
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewSorteos() throws Exception {
        // Initialize the database
        sorteosRepository.save(sorteos).block();

        int databaseSizeBeforeUpdate = Objects.requireNonNull(sorteosRepository.findAll().collectList().block()).size();

        // Update the sorteos
        Sorteos updatedSorteos = sorteosRepository.findById(sorteos.getId()).block();
        assert updatedSorteos != null;
        updatedSorteos.fechaSorteo(UPDATED_FECHA_SORTEO).codigo(UPDATED_CODIGO);
        SorteosDTO sorteosDTO = sorteosMapper.toDto(updatedSorteos);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sorteosDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sorteosDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sorteos in the database
        List<Sorteos> sorteosList = sorteosRepository.findAll().collectList().block();
        assertThat(sorteosList).hasSize(databaseSizeBeforeUpdate);
        Sorteos testSorteos = sorteosList.get(sorteosList.size() - 1);
        assertThat(testSorteos.getFechaSorteo()).isEqualTo(UPDATED_FECHA_SORTEO);
        assertThat(testSorteos.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    void putNonExistingSorteos() throws Exception {
        int databaseSizeBeforeUpdate = sorteosRepository.findAll().collectList().block().size();
        sorteos.setId(count.incrementAndGet());

        // Create the Sorteos
        SorteosDTO sorteosDTO = sorteosMapper.toDto(sorteos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sorteosDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sorteosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sorteos in the database
        List<Sorteos> sorteosList = sorteosRepository.findAll().collectList().block();
        assertThat(sorteosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSorteos() throws Exception {
        int databaseSizeBeforeUpdate = sorteosRepository.findAll().collectList().block().size();
        sorteos.setId(count.incrementAndGet());

        // Create the Sorteos
        SorteosDTO sorteosDTO = sorteosMapper.toDto(sorteos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sorteosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sorteos in the database
        List<Sorteos> sorteosList = sorteosRepository.findAll().collectList().block();
        assertThat(sorteosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSorteos() throws Exception {
        int databaseSizeBeforeUpdate = sorteosRepository.findAll().collectList().block().size();
        sorteos.setId(count.incrementAndGet());

        // Create the Sorteos
        SorteosDTO sorteosDTO = sorteosMapper.toDto(sorteos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sorteosDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sorteos in the database
        List<Sorteos> sorteosList = sorteosRepository.findAll().collectList().block();
        assertThat(sorteosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSorteosWithPatch() throws Exception {
        // Initialize the database
        sorteosRepository.save(sorteos).block();

        int databaseSizeBeforeUpdate = sorteosRepository.findAll().collectList().block().size();

        // Update the sorteos using partial update
        Sorteos partialUpdatedSorteos = new Sorteos();
        partialUpdatedSorteos.setId(sorteos.getId());

        partialUpdatedSorteos.codigo(UPDATED_CODIGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSorteos.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSorteos))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sorteos in the database
        List<Sorteos> sorteosList = sorteosRepository.findAll().collectList().block();
        assertThat(sorteosList).hasSize(databaseSizeBeforeUpdate);
        Sorteos testSorteos = sorteosList.get(sorteosList.size() - 1);
        assertThat(testSorteos.getFechaSorteo()).isEqualTo(DEFAULT_FECHA_SORTEO);
        assertThat(testSorteos.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    void fullUpdateSorteosWithPatch() throws Exception {
        // Initialize the database
        sorteosRepository.save(sorteos).block();

        int databaseSizeBeforeUpdate = sorteosRepository.findAll().collectList().block().size();

        // Update the sorteos using partial update
        Sorteos partialUpdatedSorteos = new Sorteos();
        partialUpdatedSorteos.setId(sorteos.getId());

        partialUpdatedSorteos.fechaSorteo(UPDATED_FECHA_SORTEO).codigo(UPDATED_CODIGO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSorteos.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSorteos))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sorteos in the database
        List<Sorteos> sorteosList = sorteosRepository.findAll().collectList().block();
        assertThat(sorteosList).hasSize(databaseSizeBeforeUpdate);
        Sorteos testSorteos = sorteosList.get(sorteosList.size() - 1);
        assertThat(testSorteos.getFechaSorteo()).isEqualTo(UPDATED_FECHA_SORTEO);
        assertThat(testSorteos.getCodigo()).isEqualTo(UPDATED_CODIGO);
    }

    @Test
    void patchNonExistingSorteos() throws Exception {
        int databaseSizeBeforeUpdate = sorteosRepository.findAll().collectList().block().size();
        sorteos.setId(count.incrementAndGet());

        // Create the Sorteos
        SorteosDTO sorteosDTO = sorteosMapper.toDto(sorteos);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sorteosDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sorteosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sorteos in the database
        List<Sorteos> sorteosList = sorteosRepository.findAll().collectList().block();
        assertThat(sorteosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSorteos() throws Exception {
        int databaseSizeBeforeUpdate = sorteosRepository.findAll().collectList().block().size();
        sorteos.setId(count.incrementAndGet());

        // Create the Sorteos
        SorteosDTO sorteosDTO = sorteosMapper.toDto(sorteos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sorteosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sorteos in the database
        List<Sorteos> sorteosList = sorteosRepository.findAll().collectList().block();
        assertThat(sorteosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSorteos() throws Exception {
        int databaseSizeBeforeUpdate = sorteosRepository.findAll().collectList().block().size();
        sorteos.setId(count.incrementAndGet());

        // Create the Sorteos
        SorteosDTO sorteosDTO = sorteosMapper.toDto(sorteos);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sorteosDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sorteos in the database
        List<Sorteos> sorteosList = sorteosRepository.findAll().collectList().block();
        assertThat(sorteosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSorteos() {
        // Initialize the database
        sorteosRepository.save(sorteos).block();

        int databaseSizeBeforeDelete = sorteosRepository.findAll().collectList().block().size();

        // Delete the sorteos
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sorteos.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Sorteos> sorteosList = sorteosRepository.findAll().collectList().block();
        assertThat(sorteosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

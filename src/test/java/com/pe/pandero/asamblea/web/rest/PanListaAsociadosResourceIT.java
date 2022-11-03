package com.pe.pandero.asamblea.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.pe.pandero.asamblea.IntegrationTest;
import com.pe.pandero.asamblea.domain.PanListaAsociados;
import com.pe.pandero.asamblea.repository.PanListaAsociadosRepository;
import com.pe.pandero.asamblea.service.EntityManager;
import com.pe.pandero.asamblea.service.dto.PanListaAsociadosDTO;
import com.pe.pandero.asamblea.service.mapper.PanListaAsociadosMapper;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PanListaAsociadosResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient
@WithMockUser
class PanListaAsociadosResourceIT {

    private static final Integer DEFAULT_LISTA_ASOCIADOS_ID = 1;
    private static final Integer UPDATED_LISTA_ASOCIADOS_ID = 2;

    private static final Double DEFAULT_GRUPO = 1D;
    private static final Double UPDATED_GRUPO = 2D;

    private static final Double DEFAULT_POSICION_ID = 1D;
    private static final Double UPDATED_POSICION_ID = 2D;

    private static final String DEFAULT_NOMBRE_COMPLETO = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE_COMPLETO = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO_HABIL = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO_HABIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pan-lista-asociados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PanListaAsociadosRepository panListaAsociadosRepository;

    @Autowired
    private PanListaAsociadosMapper panListaAsociadosMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private PanListaAsociados panListaAsociados;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PanListaAsociados createEntity(EntityManager em) {
        PanListaAsociados panListaAsociados = new PanListaAsociados()
            .listaAsociadosId(DEFAULT_LISTA_ASOCIADOS_ID)
            .grupo(DEFAULT_GRUPO)
            .posicionId(DEFAULT_POSICION_ID)
            .nombreCompleto(DEFAULT_NOMBRE_COMPLETO)
            .estadoHabil(DEFAULT_ESTADO_HABIL);
        return panListaAsociados;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PanListaAsociados createUpdatedEntity(EntityManager em) {
        PanListaAsociados panListaAsociados = new PanListaAsociados()
            .listaAsociadosId(UPDATED_LISTA_ASOCIADOS_ID)
            .grupo(UPDATED_GRUPO)
            .posicionId(UPDATED_POSICION_ID)
            .nombreCompleto(UPDATED_NOMBRE_COMPLETO)
            .estadoHabil(UPDATED_ESTADO_HABIL);
        return panListaAsociados;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(PanListaAsociados.class).block();
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
        panListaAsociados = createEntity(em);
    }

    @Test
    void createPanListaAsociados() throws Exception {
        int databaseSizeBeforeCreate = panListaAsociadosRepository.findAll().collectList().block().size();
        // Create the PanListaAsociados
        PanListaAsociadosDTO panListaAsociadosDTO = panListaAsociadosMapper.toDto(panListaAsociados);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(panListaAsociadosDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the PanListaAsociados in the database
        List<PanListaAsociados> panListaAsociadosList = panListaAsociadosRepository.findAll().collectList().block();
        assertThat(panListaAsociadosList).hasSize(databaseSizeBeforeCreate + 1);
        PanListaAsociados testPanListaAsociados = panListaAsociadosList.get(panListaAsociadosList.size() - 1);
        assertThat(testPanListaAsociados.getListaAsociadosId()).isEqualTo(DEFAULT_LISTA_ASOCIADOS_ID);
        assertThat(testPanListaAsociados.getGrupo()).isEqualTo(DEFAULT_GRUPO);
        assertThat(testPanListaAsociados.getPosicionId()).isEqualTo(DEFAULT_POSICION_ID);
        assertThat(testPanListaAsociados.getNombreCompleto()).isEqualTo(DEFAULT_NOMBRE_COMPLETO);
        assertThat(testPanListaAsociados.getEstadoHabil()).isEqualTo(DEFAULT_ESTADO_HABIL);
    }

    @Test
    void createPanListaAsociadosWithExistingId() throws Exception {
        // Create the PanListaAsociados with an existing ID
        panListaAsociados.setId(1L);
        PanListaAsociadosDTO panListaAsociadosDTO = panListaAsociadosMapper.toDto(panListaAsociados);

        int databaseSizeBeforeCreate = panListaAsociadosRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(panListaAsociadosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PanListaAsociados in the database
        List<PanListaAsociados> panListaAsociadosList = panListaAsociadosRepository.findAll().collectList().block();
        assertThat(panListaAsociadosList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPanListaAsociadosAsStream() {
        // Initialize the database
        panListaAsociadosRepository.save(panListaAsociados).block();

        List<PanListaAsociados> panListaAsociadosList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(PanListaAsociadosDTO.class)
            .getResponseBody()
            .map(panListaAsociadosMapper::toEntity)
            .filter(panListaAsociados::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(panListaAsociadosList).isNotNull();
        assertThat(panListaAsociadosList).hasSize(1);
        PanListaAsociados testPanListaAsociados = panListaAsociadosList.get(0);
        assertThat(testPanListaAsociados.getListaAsociadosId()).isEqualTo(DEFAULT_LISTA_ASOCIADOS_ID);
        assertThat(testPanListaAsociados.getGrupo()).isEqualTo(DEFAULT_GRUPO);
        assertThat(testPanListaAsociados.getPosicionId()).isEqualTo(DEFAULT_POSICION_ID);
        assertThat(testPanListaAsociados.getNombreCompleto()).isEqualTo(DEFAULT_NOMBRE_COMPLETO);
        assertThat(testPanListaAsociados.getEstadoHabil()).isEqualTo(DEFAULT_ESTADO_HABIL);
    }

    @Test
    void getAllPanListaAsociados() {
        // Initialize the database
        panListaAsociadosRepository.save(panListaAsociados).block();

        // Get all the panListaAsociadosList
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
            .value(hasItem(panListaAsociados.getId().intValue()))
            .jsonPath("$.[*].listaAsociadosId")
            .value(hasItem(DEFAULT_LISTA_ASOCIADOS_ID))
            .jsonPath("$.[*].grupo")
            .value(hasItem(DEFAULT_GRUPO.doubleValue()))
            .jsonPath("$.[*].posicionId")
            .value(hasItem(DEFAULT_POSICION_ID.doubleValue()))
            .jsonPath("$.[*].nombreCompleto")
            .value(hasItem(DEFAULT_NOMBRE_COMPLETO))
            .jsonPath("$.[*].estadoHabil")
            .value(hasItem(DEFAULT_ESTADO_HABIL));
    }

    @Test
    void getPanListaAsociados() {
        // Initialize the database
        panListaAsociadosRepository.save(panListaAsociados).block();

        // Get the panListaAsociados
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, panListaAsociados.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(panListaAsociados.getId().intValue()))
            .jsonPath("$.listaAsociadosId")
            .value(is(DEFAULT_LISTA_ASOCIADOS_ID))
            .jsonPath("$.grupo")
            .value(is(DEFAULT_GRUPO.doubleValue()))
            .jsonPath("$.posicionId")
            .value(is(DEFAULT_POSICION_ID.doubleValue()))
            .jsonPath("$.nombreCompleto")
            .value(is(DEFAULT_NOMBRE_COMPLETO))
            .jsonPath("$.estadoHabil")
            .value(is(DEFAULT_ESTADO_HABIL));
    }

    @Test
    void getNonExistingPanListaAsociados() {
        // Get the panListaAsociados
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putNewPanListaAsociados() throws Exception {
        // Initialize the database
        panListaAsociadosRepository.save(panListaAsociados).block();

        int databaseSizeBeforeUpdate = panListaAsociadosRepository.findAll().collectList().block().size();

        // Update the panListaAsociados
        PanListaAsociados updatedPanListaAsociados = panListaAsociadosRepository.findById(panListaAsociados.getId()).block();
        updatedPanListaAsociados
            .listaAsociadosId(UPDATED_LISTA_ASOCIADOS_ID)
            .grupo(UPDATED_GRUPO)
            .posicionId(UPDATED_POSICION_ID)
            .nombreCompleto(UPDATED_NOMBRE_COMPLETO)
            .estadoHabil(UPDATED_ESTADO_HABIL);
        PanListaAsociadosDTO panListaAsociadosDTO = panListaAsociadosMapper.toDto(updatedPanListaAsociados);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, panListaAsociadosDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(panListaAsociadosDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PanListaAsociados in the database
        List<PanListaAsociados> panListaAsociadosList = panListaAsociadosRepository.findAll().collectList().block();
        assertThat(panListaAsociadosList).hasSize(databaseSizeBeforeUpdate);
        PanListaAsociados testPanListaAsociados = panListaAsociadosList.get(panListaAsociadosList.size() - 1);
        assertThat(testPanListaAsociados.getListaAsociadosId()).isEqualTo(UPDATED_LISTA_ASOCIADOS_ID);
        assertThat(testPanListaAsociados.getGrupo()).isEqualTo(UPDATED_GRUPO);
        assertThat(testPanListaAsociados.getPosicionId()).isEqualTo(UPDATED_POSICION_ID);
        assertThat(testPanListaAsociados.getNombreCompleto()).isEqualTo(UPDATED_NOMBRE_COMPLETO);
        assertThat(testPanListaAsociados.getEstadoHabil()).isEqualTo(UPDATED_ESTADO_HABIL);
    }

    @Test
    void putNonExistingPanListaAsociados() throws Exception {
        int databaseSizeBeforeUpdate = panListaAsociadosRepository.findAll().collectList().block().size();
        panListaAsociados.setId(count.incrementAndGet());

        // Create the PanListaAsociados
        PanListaAsociadosDTO panListaAsociadosDTO = panListaAsociadosMapper.toDto(panListaAsociados);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, panListaAsociadosDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(panListaAsociadosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PanListaAsociados in the database
        List<PanListaAsociados> panListaAsociadosList = panListaAsociadosRepository.findAll().collectList().block();
        assertThat(panListaAsociadosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPanListaAsociados() throws Exception {
        int databaseSizeBeforeUpdate = panListaAsociadosRepository.findAll().collectList().block().size();
        panListaAsociados.setId(count.incrementAndGet());

        // Create the PanListaAsociados
        PanListaAsociadosDTO panListaAsociadosDTO = panListaAsociadosMapper.toDto(panListaAsociados);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(panListaAsociadosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PanListaAsociados in the database
        List<PanListaAsociados> panListaAsociadosList = panListaAsociadosRepository.findAll().collectList().block();
        assertThat(panListaAsociadosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPanListaAsociados() throws Exception {
        int databaseSizeBeforeUpdate = panListaAsociadosRepository.findAll().collectList().block().size();
        panListaAsociados.setId(count.incrementAndGet());

        // Create the PanListaAsociados
        PanListaAsociadosDTO panListaAsociadosDTO = panListaAsociadosMapper.toDto(panListaAsociados);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(panListaAsociadosDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PanListaAsociados in the database
        List<PanListaAsociados> panListaAsociadosList = panListaAsociadosRepository.findAll().collectList().block();
        assertThat(panListaAsociadosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePanListaAsociadosWithPatch() throws Exception {
        // Initialize the database
        panListaAsociadosRepository.save(panListaAsociados).block();

        int databaseSizeBeforeUpdate = panListaAsociadosRepository.findAll().collectList().block().size();

        // Update the panListaAsociados using partial update
        PanListaAsociados partialUpdatedPanListaAsociados = new PanListaAsociados();
        partialUpdatedPanListaAsociados.setId(panListaAsociados.getId());

        partialUpdatedPanListaAsociados.posicionId(UPDATED_POSICION_ID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPanListaAsociados.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPanListaAsociados))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PanListaAsociados in the database
        List<PanListaAsociados> panListaAsociadosList = panListaAsociadosRepository.findAll().collectList().block();
        assertThat(panListaAsociadosList).hasSize(databaseSizeBeforeUpdate);
        PanListaAsociados testPanListaAsociados = panListaAsociadosList.get(panListaAsociadosList.size() - 1);
        assertThat(testPanListaAsociados.getListaAsociadosId()).isEqualTo(DEFAULT_LISTA_ASOCIADOS_ID);
        assertThat(testPanListaAsociados.getGrupo()).isEqualTo(DEFAULT_GRUPO);
        assertThat(testPanListaAsociados.getPosicionId()).isEqualTo(UPDATED_POSICION_ID);
        assertThat(testPanListaAsociados.getNombreCompleto()).isEqualTo(DEFAULT_NOMBRE_COMPLETO);
        assertThat(testPanListaAsociados.getEstadoHabil()).isEqualTo(DEFAULT_ESTADO_HABIL);
    }

    @Test
    void fullUpdatePanListaAsociadosWithPatch() throws Exception {
        // Initialize the database
        panListaAsociadosRepository.save(panListaAsociados).block();

        int databaseSizeBeforeUpdate = panListaAsociadosRepository.findAll().collectList().block().size();

        // Update the panListaAsociados using partial update
        PanListaAsociados partialUpdatedPanListaAsociados = new PanListaAsociados();
        partialUpdatedPanListaAsociados.setId(panListaAsociados.getId());

        partialUpdatedPanListaAsociados
            .listaAsociadosId(UPDATED_LISTA_ASOCIADOS_ID)
            .grupo(UPDATED_GRUPO)
            .posicionId(UPDATED_POSICION_ID)
            .nombreCompleto(UPDATED_NOMBRE_COMPLETO)
            .estadoHabil(UPDATED_ESTADO_HABIL);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPanListaAsociados.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPanListaAsociados))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the PanListaAsociados in the database
        List<PanListaAsociados> panListaAsociadosList = panListaAsociadosRepository.findAll().collectList().block();
        assertThat(panListaAsociadosList).hasSize(databaseSizeBeforeUpdate);
        PanListaAsociados testPanListaAsociados = panListaAsociadosList.get(panListaAsociadosList.size() - 1);
        assertThat(testPanListaAsociados.getListaAsociadosId()).isEqualTo(UPDATED_LISTA_ASOCIADOS_ID);
        assertThat(testPanListaAsociados.getGrupo()).isEqualTo(UPDATED_GRUPO);
        assertThat(testPanListaAsociados.getPosicionId()).isEqualTo(UPDATED_POSICION_ID);
        assertThat(testPanListaAsociados.getNombreCompleto()).isEqualTo(UPDATED_NOMBRE_COMPLETO);
        assertThat(testPanListaAsociados.getEstadoHabil()).isEqualTo(UPDATED_ESTADO_HABIL);
    }

    @Test
    void patchNonExistingPanListaAsociados() throws Exception {
        int databaseSizeBeforeUpdate = panListaAsociadosRepository.findAll().collectList().block().size();
        panListaAsociados.setId(count.incrementAndGet());

        // Create the PanListaAsociados
        PanListaAsociadosDTO panListaAsociadosDTO = panListaAsociadosMapper.toDto(panListaAsociados);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, panListaAsociadosDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(panListaAsociadosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PanListaAsociados in the database
        List<PanListaAsociados> panListaAsociadosList = panListaAsociadosRepository.findAll().collectList().block();
        assertThat(panListaAsociadosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPanListaAsociados() throws Exception {
        int databaseSizeBeforeUpdate = panListaAsociadosRepository.findAll().collectList().block().size();
        panListaAsociados.setId(count.incrementAndGet());

        // Create the PanListaAsociados
        PanListaAsociadosDTO panListaAsociadosDTO = panListaAsociadosMapper.toDto(panListaAsociados);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(panListaAsociadosDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the PanListaAsociados in the database
        List<PanListaAsociados> panListaAsociadosList = panListaAsociadosRepository.findAll().collectList().block();
        assertThat(panListaAsociadosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPanListaAsociados() throws Exception {
        int databaseSizeBeforeUpdate = panListaAsociadosRepository.findAll().collectList().block().size();
        panListaAsociados.setId(count.incrementAndGet());

        // Create the PanListaAsociados
        PanListaAsociadosDTO panListaAsociadosDTO = panListaAsociadosMapper.toDto(panListaAsociados);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(panListaAsociadosDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the PanListaAsociados in the database
        List<PanListaAsociados> panListaAsociadosList = panListaAsociadosRepository.findAll().collectList().block();
        assertThat(panListaAsociadosList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePanListaAsociados() {
        // Initialize the database
        panListaAsociadosRepository.save(panListaAsociados).block();

        int databaseSizeBeforeDelete = panListaAsociadosRepository.findAll().collectList().block().size();

        // Delete the panListaAsociados
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, panListaAsociados.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<PanListaAsociados> panListaAsociadosList = panListaAsociadosRepository.findAll().collectList().block();
        assertThat(panListaAsociadosList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

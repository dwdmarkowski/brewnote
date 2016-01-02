package org.dmarkowski.brewnote.web.rest;

import org.dmarkowski.brewnote.Application;
import org.dmarkowski.brewnote.domain.Hop;
import org.dmarkowski.brewnote.repository.HopRepository;
import org.dmarkowski.brewnote.web.rest.dto.HopDTO;
import org.dmarkowski.brewnote.web.rest.mapper.HopMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the HopResource REST controller.
 *
 * @see HopResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class HopResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Double DEFAULT_ALFA_ACIDS = 1D;
    private static final Double UPDATED_ALFA_ACIDS = 2D;
    private static final String DEFAULT_HOP_FORM = "AAAAA";
    private static final String UPDATED_HOP_FORM = "BBBBB";

    @Inject
    private HopRepository hopRepository;

    @Inject
    private HopMapper hopMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHopMockMvc;

    private Hop hop;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HopResource hopResource = new HopResource();
        ReflectionTestUtils.setField(hopResource, "hopRepository", hopRepository);
        ReflectionTestUtils.setField(hopResource, "hopMapper", hopMapper);
        this.restHopMockMvc = MockMvcBuilders.standaloneSetup(hopResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hop = new Hop();
        hop.setName(DEFAULT_NAME);
        hop.setAlfaAcids(DEFAULT_ALFA_ACIDS);
        hop.setHopForm(DEFAULT_HOP_FORM);
    }

    @Test
    @Transactional
    public void createHop() throws Exception {
        int databaseSizeBeforeCreate = hopRepository.findAll().size();

        // Create the Hop
        HopDTO hopDTO = hopMapper.hopToHopDTO(hop);

        restHopMockMvc.perform(post("/api/hops")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hopDTO)))
                .andExpect(status().isCreated());

        // Validate the Hop in the database
        List<Hop> hops = hopRepository.findAll();
        assertThat(hops).hasSize(databaseSizeBeforeCreate + 1);
        Hop testHop = hops.get(hops.size() - 1);
        assertThat(testHop.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHop.getAlfaAcids()).isEqualTo(DEFAULT_ALFA_ACIDS);
        assertThat(testHop.getHopForm()).isEqualTo(DEFAULT_HOP_FORM);
    }

    @Test
    @Transactional
    public void getAllHops() throws Exception {
        // Initialize the database
        hopRepository.saveAndFlush(hop);

        // Get all the hops
        restHopMockMvc.perform(get("/api/hops"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hop.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].alfaAcids").value(hasItem(DEFAULT_ALFA_ACIDS.doubleValue())))
                .andExpect(jsonPath("$.[*].hopForm").value(hasItem(DEFAULT_HOP_FORM.toString())));
    }

    @Test
    @Transactional
    public void getHop() throws Exception {
        // Initialize the database
        hopRepository.saveAndFlush(hop);

        // Get the hop
        restHopMockMvc.perform(get("/api/hops/{id}", hop.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hop.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.alfaAcids").value(DEFAULT_ALFA_ACIDS.doubleValue()))
            .andExpect(jsonPath("$.hopForm").value(DEFAULT_HOP_FORM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHop() throws Exception {
        // Get the hop
        restHopMockMvc.perform(get("/api/hops/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHop() throws Exception {
        // Initialize the database
        hopRepository.saveAndFlush(hop);

		int databaseSizeBeforeUpdate = hopRepository.findAll().size();

        // Update the hop
        hop.setName(UPDATED_NAME);
        hop.setAlfaAcids(UPDATED_ALFA_ACIDS);
        hop.setHopForm(UPDATED_HOP_FORM);
        HopDTO hopDTO = hopMapper.hopToHopDTO(hop);

        restHopMockMvc.perform(put("/api/hops")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hopDTO)))
                .andExpect(status().isOk());

        // Validate the Hop in the database
        List<Hop> hops = hopRepository.findAll();
        assertThat(hops).hasSize(databaseSizeBeforeUpdate);
        Hop testHop = hops.get(hops.size() - 1);
        assertThat(testHop.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHop.getAlfaAcids()).isEqualTo(UPDATED_ALFA_ACIDS);
        assertThat(testHop.getHopForm()).isEqualTo(UPDATED_HOP_FORM);
    }

    @Test
    @Transactional
    public void deleteHop() throws Exception {
        // Initialize the database
        hopRepository.saveAndFlush(hop);

		int databaseSizeBeforeDelete = hopRepository.findAll().size();

        // Get the hop
        restHopMockMvc.perform(delete("/api/hops/{id}", hop.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Hop> hops = hopRepository.findAll();
        assertThat(hops).hasSize(databaseSizeBeforeDelete - 1);
    }
}

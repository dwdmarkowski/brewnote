package org.dmarkowski.brewnote.web.rest;

import org.dmarkowski.brewnote.Application;
import org.dmarkowski.brewnote.domain.Malt;
import org.dmarkowski.brewnote.repository.MaltRepository;
import org.dmarkowski.brewnote.web.rest.dto.MaltDTO;
import org.dmarkowski.brewnote.web.rest.mapper.MaltMapper;

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
 * Test class for the MaltResource REST controller.
 *
 * @see MaltResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MaltResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Inject
    private MaltRepository maltRepository;

    @Inject
    private MaltMapper maltMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMaltMockMvc;

    private Malt malt;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MaltResource maltResource = new MaltResource();
        ReflectionTestUtils.setField(maltResource, "maltRepository", maltRepository);
        ReflectionTestUtils.setField(maltResource, "maltMapper", maltMapper);
        this.restMaltMockMvc = MockMvcBuilders.standaloneSetup(maltResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        malt = new Malt();
        malt.setName(DEFAULT_NAME);
        malt.setAmount(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createMalt() throws Exception {
        int databaseSizeBeforeCreate = maltRepository.findAll().size();

        // Create the Malt
        MaltDTO maltDTO = maltMapper.maltToMaltDTO(malt);

        restMaltMockMvc.perform(post("/api/malts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(maltDTO)))
                .andExpect(status().isCreated());

        // Validate the Malt in the database
        List<Malt> malts = maltRepository.findAll();
        assertThat(malts).hasSize(databaseSizeBeforeCreate + 1);
        Malt testMalt = malts.get(malts.size() - 1);
        assertThat(testMalt.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMalt.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllMalts() throws Exception {
        // Initialize the database
        maltRepository.saveAndFlush(malt);

        // Get all the malts
        restMaltMockMvc.perform(get("/api/malts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(malt.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }

    @Test
    @Transactional
    public void getMalt() throws Exception {
        // Initialize the database
        maltRepository.saveAndFlush(malt);

        // Get the malt
        restMaltMockMvc.perform(get("/api/malts/{id}", malt.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(malt.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMalt() throws Exception {
        // Get the malt
        restMaltMockMvc.perform(get("/api/malts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMalt() throws Exception {
        // Initialize the database
        maltRepository.saveAndFlush(malt);

		int databaseSizeBeforeUpdate = maltRepository.findAll().size();

        // Update the malt
        malt.setName(UPDATED_NAME);
        malt.setAmount(UPDATED_AMOUNT);
        MaltDTO maltDTO = maltMapper.maltToMaltDTO(malt);

        restMaltMockMvc.perform(put("/api/malts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(maltDTO)))
                .andExpect(status().isOk());

        // Validate the Malt in the database
        List<Malt> malts = maltRepository.findAll();
        assertThat(malts).hasSize(databaseSizeBeforeUpdate);
        Malt testMalt = malts.get(malts.size() - 1);
        assertThat(testMalt.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMalt.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void deleteMalt() throws Exception {
        // Initialize the database
        maltRepository.saveAndFlush(malt);

		int databaseSizeBeforeDelete = maltRepository.findAll().size();

        // Get the malt
        restMaltMockMvc.perform(delete("/api/malts/{id}", malt.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Malt> malts = maltRepository.findAll();
        assertThat(malts).hasSize(databaseSizeBeforeDelete - 1);
    }
}

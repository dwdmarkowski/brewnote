package org.dmarkowski.brewnote.web.rest;

import org.dmarkowski.brewnote.Application;
import org.dmarkowski.brewnote.domain.Mashing;
import org.dmarkowski.brewnote.repository.MashingRepository;
import org.dmarkowski.brewnote.web.rest.dto.MashingDTO;
import org.dmarkowski.brewnote.web.rest.mapper.MashingMapper;

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
 * Test class for the MashingResource REST controller.
 *
 * @see MashingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MashingResourceIntTest {


    private static final Integer DEFAULT_TIME = 1;
    private static final Integer UPDATED_TIME = 2;

    private static final Integer DEFAULT_TEMPERATURE = 1;
    private static final Integer UPDATED_TEMPERATURE = 2;

    @Inject
    private MashingRepository mashingRepository;

    @Inject
    private MashingMapper mashingMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMashingMockMvc;

    private Mashing mashing;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MashingResource mashingResource = new MashingResource();
        ReflectionTestUtils.setField(mashingResource, "mashingRepository", mashingRepository);
        ReflectionTestUtils.setField(mashingResource, "mashingMapper", mashingMapper);
        this.restMashingMockMvc = MockMvcBuilders.standaloneSetup(mashingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        mashing = new Mashing();
        mashing.setTime(DEFAULT_TIME);
        mashing.setTemperature(DEFAULT_TEMPERATURE);
    }

    @Test
    @Transactional
    public void createMashing() throws Exception {
        int databaseSizeBeforeCreate = mashingRepository.findAll().size();

        // Create the Mashing
        MashingDTO mashingDTO = mashingMapper.mashingToMashingDTO(mashing);

        restMashingMockMvc.perform(post("/api/mashings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mashingDTO)))
                .andExpect(status().isCreated());

        // Validate the Mashing in the database
        List<Mashing> mashings = mashingRepository.findAll();
        assertThat(mashings).hasSize(databaseSizeBeforeCreate + 1);
        Mashing testMashing = mashings.get(mashings.size() - 1);
        assertThat(testMashing.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testMashing.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
    }

    @Test
    @Transactional
    public void getAllMashings() throws Exception {
        // Initialize the database
        mashingRepository.saveAndFlush(mashing);

        // Get all the mashings
        restMashingMockMvc.perform(get("/api/mashings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mashing.getId().intValue())))
                .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME)))
                .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE)));
    }

    @Test
    @Transactional
    public void getMashing() throws Exception {
        // Initialize the database
        mashingRepository.saveAndFlush(mashing);

        // Get the mashing
        restMashingMockMvc.perform(get("/api/mashings/{id}", mashing.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(mashing.getId().intValue()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE));
    }

    @Test
    @Transactional
    public void getNonExistingMashing() throws Exception {
        // Get the mashing
        restMashingMockMvc.perform(get("/api/mashings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMashing() throws Exception {
        // Initialize the database
        mashingRepository.saveAndFlush(mashing);

		int databaseSizeBeforeUpdate = mashingRepository.findAll().size();

        // Update the mashing
        mashing.setTime(UPDATED_TIME);
        mashing.setTemperature(UPDATED_TEMPERATURE);
        MashingDTO mashingDTO = mashingMapper.mashingToMashingDTO(mashing);

        restMashingMockMvc.perform(put("/api/mashings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mashingDTO)))
                .andExpect(status().isOk());

        // Validate the Mashing in the database
        List<Mashing> mashings = mashingRepository.findAll();
        assertThat(mashings).hasSize(databaseSizeBeforeUpdate);
        Mashing testMashing = mashings.get(mashings.size() - 1);
        assertThat(testMashing.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testMashing.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
    }

    @Test
    @Transactional
    public void deleteMashing() throws Exception {
        // Initialize the database
        mashingRepository.saveAndFlush(mashing);

		int databaseSizeBeforeDelete = mashingRepository.findAll().size();

        // Get the mashing
        restMashingMockMvc.perform(delete("/api/mashings/{id}", mashing.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Mashing> mashings = mashingRepository.findAll();
        assertThat(mashings).hasSize(databaseSizeBeforeDelete - 1);
    }
}

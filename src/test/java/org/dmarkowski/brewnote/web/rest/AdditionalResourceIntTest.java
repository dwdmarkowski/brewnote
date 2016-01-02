package org.dmarkowski.brewnote.web.rest;

import org.dmarkowski.brewnote.Application;
import org.dmarkowski.brewnote.domain.Additional;
import org.dmarkowski.brewnote.repository.AdditionalRepository;
import org.dmarkowski.brewnote.web.rest.dto.AdditionalDTO;
import org.dmarkowski.brewnote.web.rest.mapper.AdditionalMapper;

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
 * Test class for the AdditionalResource REST controller.
 *
 * @see AdditionalResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AdditionalResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private AdditionalRepository additionalRepository;

    @Inject
    private AdditionalMapper additionalMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAdditionalMockMvc;

    private Additional additional;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AdditionalResource additionalResource = new AdditionalResource();
        ReflectionTestUtils.setField(additionalResource, "additionalRepository", additionalRepository);
        ReflectionTestUtils.setField(additionalResource, "additionalMapper", additionalMapper);
        this.restAdditionalMockMvc = MockMvcBuilders.standaloneSetup(additionalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        additional = new Additional();
        additional.setName(DEFAULT_NAME);
        additional.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createAdditional() throws Exception {
        int databaseSizeBeforeCreate = additionalRepository.findAll().size();

        // Create the Additional
        AdditionalDTO additionalDTO = additionalMapper.additionalToAdditionalDTO(additional);

        restAdditionalMockMvc.perform(post("/api/additionals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(additionalDTO)))
                .andExpect(status().isCreated());

        // Validate the Additional in the database
        List<Additional> additionals = additionalRepository.findAll();
        assertThat(additionals).hasSize(databaseSizeBeforeCreate + 1);
        Additional testAdditional = additionals.get(additionals.size() - 1);
        assertThat(testAdditional.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAdditional.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAdditionals() throws Exception {
        // Initialize the database
        additionalRepository.saveAndFlush(additional);

        // Get all the additionals
        restAdditionalMockMvc.perform(get("/api/additionals"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(additional.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getAdditional() throws Exception {
        // Initialize the database
        additionalRepository.saveAndFlush(additional);

        // Get the additional
        restAdditionalMockMvc.perform(get("/api/additionals/{id}", additional.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(additional.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAdditional() throws Exception {
        // Get the additional
        restAdditionalMockMvc.perform(get("/api/additionals/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdditional() throws Exception {
        // Initialize the database
        additionalRepository.saveAndFlush(additional);

		int databaseSizeBeforeUpdate = additionalRepository.findAll().size();

        // Update the additional
        additional.setName(UPDATED_NAME);
        additional.setDescription(UPDATED_DESCRIPTION);
        AdditionalDTO additionalDTO = additionalMapper.additionalToAdditionalDTO(additional);

        restAdditionalMockMvc.perform(put("/api/additionals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(additionalDTO)))
                .andExpect(status().isOk());

        // Validate the Additional in the database
        List<Additional> additionals = additionalRepository.findAll();
        assertThat(additionals).hasSize(databaseSizeBeforeUpdate);
        Additional testAdditional = additionals.get(additionals.size() - 1);
        assertThat(testAdditional.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAdditional.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteAdditional() throws Exception {
        // Initialize the database
        additionalRepository.saveAndFlush(additional);

		int databaseSizeBeforeDelete = additionalRepository.findAll().size();

        // Get the additional
        restAdditionalMockMvc.perform(delete("/api/additionals/{id}", additional.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Additional> additionals = additionalRepository.findAll();
        assertThat(additionals).hasSize(databaseSizeBeforeDelete - 1);
    }
}

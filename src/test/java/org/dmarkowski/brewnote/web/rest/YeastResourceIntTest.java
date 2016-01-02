package org.dmarkowski.brewnote.web.rest;

import org.dmarkowski.brewnote.Application;
import org.dmarkowski.brewnote.domain.Yeast;
import org.dmarkowski.brewnote.repository.YeastRepository;
import org.dmarkowski.brewnote.web.rest.dto.YeastDTO;
import org.dmarkowski.brewnote.web.rest.mapper.YeastMapper;

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
 * Test class for the YeastResource REST controller.
 *
 * @see YeastResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class YeastResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Integer DEFAULT_TEMPERATURE = 1;
    private static final Integer UPDATED_TEMPERATURE = 2;

    private static final Integer DEFAULT_DAYS = 1;
    private static final Integer UPDATED_DAYS = 2;

    @Inject
    private YeastRepository yeastRepository;

    @Inject
    private YeastMapper yeastMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restYeastMockMvc;

    private Yeast yeast;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        YeastResource yeastResource = new YeastResource();
        ReflectionTestUtils.setField(yeastResource, "yeastRepository", yeastRepository);
        ReflectionTestUtils.setField(yeastResource, "yeastMapper", yeastMapper);
        this.restYeastMockMvc = MockMvcBuilders.standaloneSetup(yeastResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        yeast = new Yeast();
        yeast.setName(DEFAULT_NAME);
        yeast.setTemperature(DEFAULT_TEMPERATURE);
        yeast.setDays(DEFAULT_DAYS);
    }

    @Test
    @Transactional
    public void createYeast() throws Exception {
        int databaseSizeBeforeCreate = yeastRepository.findAll().size();

        // Create the Yeast
        YeastDTO yeastDTO = yeastMapper.yeastToYeastDTO(yeast);

        restYeastMockMvc.perform(post("/api/yeasts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(yeastDTO)))
                .andExpect(status().isCreated());

        // Validate the Yeast in the database
        List<Yeast> yeasts = yeastRepository.findAll();
        assertThat(yeasts).hasSize(databaseSizeBeforeCreate + 1);
        Yeast testYeast = yeasts.get(yeasts.size() - 1);
        assertThat(testYeast.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testYeast.getTemperature()).isEqualTo(DEFAULT_TEMPERATURE);
        assertThat(testYeast.getDays()).isEqualTo(DEFAULT_DAYS);
    }

    @Test
    @Transactional
    public void getAllYeasts() throws Exception {
        // Initialize the database
        yeastRepository.saveAndFlush(yeast);

        // Get all the yeasts
        restYeastMockMvc.perform(get("/api/yeasts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(yeast.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].temperature").value(hasItem(DEFAULT_TEMPERATURE)))
                .andExpect(jsonPath("$.[*].days").value(hasItem(DEFAULT_DAYS)));
    }

    @Test
    @Transactional
    public void getYeast() throws Exception {
        // Initialize the database
        yeastRepository.saveAndFlush(yeast);

        // Get the yeast
        restYeastMockMvc.perform(get("/api/yeasts/{id}", yeast.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(yeast.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.temperature").value(DEFAULT_TEMPERATURE))
            .andExpect(jsonPath("$.days").value(DEFAULT_DAYS));
    }

    @Test
    @Transactional
    public void getNonExistingYeast() throws Exception {
        // Get the yeast
        restYeastMockMvc.perform(get("/api/yeasts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateYeast() throws Exception {
        // Initialize the database
        yeastRepository.saveAndFlush(yeast);

		int databaseSizeBeforeUpdate = yeastRepository.findAll().size();

        // Update the yeast
        yeast.setName(UPDATED_NAME);
        yeast.setTemperature(UPDATED_TEMPERATURE);
        yeast.setDays(UPDATED_DAYS);
        YeastDTO yeastDTO = yeastMapper.yeastToYeastDTO(yeast);

        restYeastMockMvc.perform(put("/api/yeasts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(yeastDTO)))
                .andExpect(status().isOk());

        // Validate the Yeast in the database
        List<Yeast> yeasts = yeastRepository.findAll();
        assertThat(yeasts).hasSize(databaseSizeBeforeUpdate);
        Yeast testYeast = yeasts.get(yeasts.size() - 1);
        assertThat(testYeast.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testYeast.getTemperature()).isEqualTo(UPDATED_TEMPERATURE);
        assertThat(testYeast.getDays()).isEqualTo(UPDATED_DAYS);
    }

    @Test
    @Transactional
    public void deleteYeast() throws Exception {
        // Initialize the database
        yeastRepository.saveAndFlush(yeast);

		int databaseSizeBeforeDelete = yeastRepository.findAll().size();

        // Get the yeast
        restYeastMockMvc.perform(delete("/api/yeasts/{id}", yeast.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Yeast> yeasts = yeastRepository.findAll();
        assertThat(yeasts).hasSize(databaseSizeBeforeDelete - 1);
    }
}

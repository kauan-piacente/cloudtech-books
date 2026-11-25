package com.ctbook.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ctbook.api.controller.BookController;

@WebMvcTest({BookController.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	// Listar livros
	@Test
	void testarListarLivros() throws Exception {
		mockMvc.perform(get("/books"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.length()").value(1))
		.andExpect(jsonPath("$[0].title").value("Dom Quixote"));
	}

	// Listar Livro por ID
	@Test
	void testarListarLivroPorId() throws Exception{
		mockMvc.perform(get("/books/d5c4f1f4-9b46-4bd6-8073-ef7eba30b2ac"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.id").value("d5c4f1f4-9b46-4bd6-8073-ef7eba30b2ac"))
		.andExpect(jsonPath("$.title").value("Dom Quixote"))
		.andExpect(jsonPath("$.author").value("Miguel de Cervantes"))
		.andExpect(jsonPath("$.publisher").value("Francisco de Robles"))
		.andExpect(jsonPath("$.year").value("1605"))
		.andExpect(jsonPath("$.category").value("Romance de cavalaria / Sátira"))
		.andExpect(jsonPath("$.status").value("Available"));
	}

	// Cadastrar Livro
	@Test
	void testeCadastrarLivro() throws Exception{
		String livro = """
		{
			"title": "O Hobbit",
			"author": "J.R.R. Tolkien",
			"publisher": "George Allen & Unwin",
			"year": "1937",
			"category": "Fantasia",
			"status": "Available"
		}
		""";

		mockMvc.perform(post("/books/register")
		.contentType("application/json")
		.content(livro))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.title").value("O Hobbit"))
		.andExpect(jsonPath("$.author").value("J.R.R. Tolkien"))
		.andExpect(jsonPath("$.publisher").value("George Allen & Unwin"))
		.andExpect(jsonPath("$.year").value("1937"))
		.andExpect(jsonPath("$.category").value("Fantasia"))
		.andExpect(jsonPath("$.status").value("Available"));
	}

	// Atualizar Livro
	@Test
	void testeAtualizarLivro() throws Exception{
		String livroAtualizado = """
			{
				"title": "Dom Quixote - ATUALIZADO",
    			"author": "Miguel de Cervantes - ATUALIZADO",
    			"publisher": "Francisco de Robles",
    			"year": "1605",
    			"category": "Romance de cavalaria / Sátira",
    			"status": "Available"
  }
			}
			""";

			mockMvc.perform(put("/books/update/d5c4f1f4-9b46-4bd6-8073-ef7eba30b2ac")
			.contentType("application/json")
			.content(livroAtualizado))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value("d5c4f1f4-9b46-4bd6-8073-ef7eba30b2ac"))
			.andExpect(jsonPath("$.title").value("Dom Quixote - ATUALIZADO"))
			.andExpect(jsonPath("$.author").value("Miguel de Cervantes - ATUALIZADO"))
			.andExpect(jsonPath("$.publisher").value("Francisco de Robles"))
			.andExpect(jsonPath("$.year").value("1605"))
			.andExpect(jsonPath("$.category").value("Romance de cavalaria / Sátira"))
			.andExpect(jsonPath("$.status").value("Available"));
	}

	// Verificar inexistência
	@Test
	void testarLivroInexistent() throws Exception{
		mockMvc.perform(get("/books/00000000-0000-0000-0000-000000000000"))
		.andExpect(status().isNotFound());
	}

}

package api.bookstore.catalog_service.service;

import api.bookstore.catalog_service.exception.CustomException;
import api.bookstore.catalog_service.models.Author;
import api.bookstore.catalog_service.models.AuthorDTO;
import api.bookstore.catalog_service.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    public AuthorDTO createAuthor(AuthorDTO authorDTO){
        validate(authorDTO);
        Author author = new Author();
        author.setName(authorDTO.getName());
        author.setBio(authorDTO.getBio());
        Author saved = authorRepository.save(author);
        return new AuthorDTO(saved.getId(), saved.getName(), saved.getBio());
    }

    public List<AuthorDTO> getAllAuthors(){
        return authorRepository.findAll().stream()
        .map(author -> new AuthorDTO(author.getId(), author.getName(), author.getBio()))
        .toList();
    }

    public AuthorDTO getAuthorById(UUID id){
        Author author = authorRepository.findById(id)
        .orElseThrow(() -> new CustomException("Author not found", HttpStatus.NOT_FOUND));
        return new AuthorDTO(author.getId(), author.getName(), author.getBio());
    }

    public AuthorDTO updateAuthor(UUID id, AuthorDTO authorDTO){
        validate(authorDTO);
        Author author = authorRepository.findById(id)
        .orElseThrow(() -> new CustomException("Author not found", HttpStatus.NOT_FOUND));

        author.setName(authorDTO.getName());
        author.setBio(authorDTO.getBio());
        Author updated = authorRepository.save(author);
        return new AuthorDTO(updated.getId(), updated.getName(), updated.getBio());
    }

    public void deleteAuthor(UUID id){
        if(!authorRepository.existsById(id))
            throw new CustomException("Author not found", HttpStatus.NOT_FOUND);
        
        authorRepository.deleteById(id);
    }

    private void validate(AuthorDTO authorDTO){
        if(authorDTO == null || authorDTO.getName() == null || authorDTO.getName().isBlank()){
            throw new CustomException("Author name is required", HttpStatus.BAD_REQUEST);
        }
    }

}

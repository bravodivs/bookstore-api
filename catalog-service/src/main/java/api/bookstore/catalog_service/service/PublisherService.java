package api.bookstore.catalog_service.service;

import api.bookstore.catalog_service.controllers.PublisherController;
import api.bookstore.catalog_service.exception.CustomException;
import api.bookstore.catalog_service.models.Publisher;
import api.bookstore.catalog_service.models.PublisherDTO;
import api.bookstore.catalog_service.repository.PublisherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    public PublisherService(PublisherRepository publisherRepository){
        this.publisherRepository = publisherRepository;
    }

    public PublisherDTO createPublisher(PublisherDTO publisherDTO){
        validate(publisherDTO);
        Publisher publisher = new Publisher();
        publisher.setName(publisherDTO.getName());
        publisher.setAddress(publisherDTO.getAddress());
        Publisher saved = publisherRepository.save(publisher);
        return new PublisherDTO(saved.getId(), saved.getName(), saved.getAddress());
    }

    public PublisherDTO getPublisherById(UUID id){
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new CustomException("Publisher not found", HttpStatus.NOT_FOUND));
        return new PublisherDTO(publisher.getId(), publisher.getName(), publisher.getAddress());
    }

    public List<PublisherDTO> getAllPublishers(){
        return publisherRepository.findAll().stream()
                .map(publisher -> new PublisherDTO(publisher.getId(), publisher.getName(), publisher.getAddress()))
                .toList();
    }

    public PublisherDTO updatePublisher(UUID id, PublisherDTO publisherDTO){
        validate(publisherDTO);
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new CustomException("Publisher not found", HttpStatus.NOT_FOUND));
        publisher.setName(publisherDTO.getName());
        publisher.setAddress(publisherDTO.getAddress());
        Publisher updated = publisherRepository.save(publisher);
        return new PublisherDTO(updated.getId(), updated.getName(), updated.getAddress());
    }

    public void deletePublisher(UUID id){
        if (!publisherRepository.existsById(id))
            throw new CustomException("Publisher not found", HttpStatus.NOT_FOUND);

        publisherRepository.deleteById(id);
    }

    private void validate(PublisherDTO publisherDTO){
        if (publisherDTO == null || publisherDTO.getName() == null || publisherDTO.getName().isBlank()){
            throw new CustomException("Publisher name is required", HttpStatus.BAD_REQUEST);
        }
    }
}

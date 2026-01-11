package proyecto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import proyecto.entity.Noticia;
import proyecto.respository.NoticiaRepository;

@Service
public class NoticiaService {

    @Autowired
    private NoticiaRepository noticiaRepository;

    public List<Noticia> findAll() {
        return noticiaRepository.findAll();
    }

    public Optional<Noticia> findById(int id) {
        return noticiaRepository.findById(id);
    }

    public Noticia save(Noticia newNoticia) {
        return noticiaRepository.save(newNoticia);
    }

    public Noticia update(int idNoticia, Noticia updatedNoticia) {
    	 Optional<Noticia> noticia = noticiaRepository.findById(idNoticia);
         if (noticia.isPresent()) {
        	noticia.get().setCuerpoNoticia(updatedNoticia.getCuerpoNoticia());
        	noticia.get().setEncabezado(updatedNoticia.getEncabezado());
        	noticia.get().setImagenNoticia(updatedNoticia.getImagenNoticia());

            return noticiaRepository.save(updatedNoticia);
        }
        return null;
    }

    public boolean deleteById(int id) {
        Optional<Noticia> noticia = noticiaRepository.findById(id);
        if (noticia.isPresent()) {
            noticiaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
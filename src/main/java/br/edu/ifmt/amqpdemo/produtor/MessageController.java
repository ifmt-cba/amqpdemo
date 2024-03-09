package br.edu.ifmt.amqpdemo.produtor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("olamundo")
public class MessageController {
    
    private MessageProducer produtor;

    @Autowired
    public MessageController(MessageProducer produtor) {
      this.produtor = produtor;
    }    

    @GetMapping
    public String executar() {
      produtor.enviarMensagemApenasParaAlice("Olá Alice!");
      produtor.enviarMensagemApenasParaBob("Olá Bob!");
      produtor.enviarMensagemApenasParaCarla("Olá Carla!");
      produtor.enviarMensagemParaTodos("Olá Todo Mundo!!!");
      produtor.enviarMensagemParaMulheres("Olá Mulheres!");
      produtor.enviarMensagemParaHomens("Olá Homens!");
      produtor.enviarMensagemParaAposentados("Olá Aposentados!");
      produtor.enviarMensagemParaMulheresAtivas("Olá Mulheres Ativas!");
      produtor.enviarMensagemErrada("Olá Daniel!");
      produtor.enviarMensagemParaAliceComTempoDeVidaBaixo("Olá Atrasado Alice!");
      
      return "OK";
    }
}

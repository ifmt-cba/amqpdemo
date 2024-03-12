package br.edu.ifmt.amqpdemo.produtor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
    public String executar() throws InterruptedException, ExecutionException, TimeoutException {
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
      System.out.println("de.alice?: " + produtor.descobrirQuemEh("de.alice"));
      System.out.println("Quem é vc Async?: " + produtor.descobrirQuemEhDepois());
      produtor.descobrirQuemEhQuandoQuiserResponder();
      
      return "OK";
    }
}

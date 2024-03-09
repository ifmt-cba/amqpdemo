package br.edu.ifmt.amqpdemo.produtor;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    RabbitTemplate template;

    public MessageProducer(RabbitTemplate template) {
        this.template = template;
    }

    public void enviarMensagemApenasParaAlice(String msg) {
        template.convertAndSend("de.message","rk.alice",msg);
    }

    public void enviarMensagemApenasParaBob(String msg) {
        template.convertAndSend("de.message","rk.bob",msg);
    }

    public void enviarMensagemApenasParaCarla(String msg) {
        template.convertAndSend("de.message","rk.carla",msg);
    }

    public void enviarMensagemParaTodos(String msg) {
        template.convertAndSend("fe.message","",msg);        
    }

    public void enviarMensagemParaMulheres(String msg) {
        template.convertAndSend("te.message","rk.naoimporta.mulher",msg);
    }

    public void enviarMensagemParaHomens(String msg) {
        template.convertAndSend("te.message","rk.naoimporta.homem",msg);
    }

    public void enviarMensagemParaMulheresAtivas(String msg) {
        MessageProperties propriedades = MessagePropertiesBuilder.newInstance()
                    .setHeader("sexo", "feminino")
                    .setHeader("situacao", "ativo")
                    .build();

        Message mensagem = new Message(msg.getBytes(),propriedades);

        template.convertAndSend("he.message","",mensagem);
    }
    
    public void enviarMensagemParaAposentados(String msg) {
        MessageProperties propriedades = MessagePropertiesBuilder.newInstance()
                    .setHeader("situacao", "aposentado")
                    .build();

        Message mensagem = new Message(msg.getBytes(),propriedades);

        template.convertAndSend("he.message","",mensagem);
    }

    public void enviarMensagemErrada(String msg) {
        template.convertAndSend("de.message","rk.daniel",msg);
    }

    public void enviarMensagemParaAliceComTempoDeVidaBaixo(String msg) {
        template.convertAndSend("de.message","rk.alice",msg,
            message -> {
                message.getMessageProperties().setExpiration(String.valueOf(1000));
                return message;
            });        
    }
    
}

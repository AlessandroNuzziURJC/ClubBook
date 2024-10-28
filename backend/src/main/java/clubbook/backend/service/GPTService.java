package clubbook.backend.service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.theokanning.openai.utils.TikTokensUtil.ModelEnum.GPT_3_5_TURBO;


@Service
public class GPTService {

    private static final String TOKEN = "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";
    private OpenAiService openAiService;

    public GPTService() {
        this.openAiService = new OpenAiService(TOKEN);
    }

    public String generateResponse(String prompt) throws Exception {
        List<ChatMessage> messages = new ArrayList<>();
        String promptJsonFormat = "Devuelve únicamente un objeto JSON en español en el siguiente formato y sin ningún texto adicional ni caracteres especiales: " +
                "{\n" +
                "  \"warmUpExercises\": [],\n" +
                "  \"specificExercises\": [],\n" +
                "  \"finalExercises\": []\n" +
                "}\n" +
                "\n" +
                "Diseña cada sección del JSON de la siguiente manera:\n" +
                "\n" +
                "- **warmUpExercises**: Incluye 3-5 ejercicios para calentar todo el cuerpo, preferiblemente usando ejercicios variados cada vez.\n" +
                "- **specificExercises**: Incluye 3-5 técnicas de nivel y adecuadas a la edad que se indique para esta clase. Si existen sesiones anteriores, utiliza esta información para sugerir nuevos ejercicios que no se repitan en esta sección.\n" +
                "- **finalExercises**: Si es una clase para niños, sugiere juegos; si es para adultos, sugiere estiramientos de enfriamiento. Los juegos y estiramientos pueden repetirse o puedes sugerir nuevos.\n" +
                "\n" +
                "### Ejemplo de respuesta esperada:\n" +
                "{\n" +
                "  \"warmUpExercises\": [\"Caminata ligera\", \"Saltos en el lugar\", \"Estiramientos de brazos\"],\n" +
                "  \"specificExercises\": [\"Técnica de caída (Ukemi)\", \"Posturas fundamentales (Kamae)\", \"Movimientos de piernas\"],\n" +
                "  \"finalExercises\": [\"Juego de equipo\", \"Estiramientos básicos\"]\n" +
                "}\n" +
                "\n" +
                "Nota: Evita texto explicativo o caracteres adicionales fuera del JSON.";

        messages.add(new ChatMessage(ChatMessageRole.USER.value(), promptJsonFormat));
        ChatMessage msg = new ChatMessage(ChatMessageRole.USER.value(), prompt);
        messages.add(msg);
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model(GPT_3_5_TURBO.getName())
                .messages(messages)
                .build();
        ChatCompletionResult result = openAiService.createChatCompletion(chatCompletionRequest);
        long usedTokens = result.getUsage().getTotalTokens();
        ChatMessage response = result.getChoices().get(0).getMessage();

        return response.getContent();
    }
}

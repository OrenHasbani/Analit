package io.benchuk.interceptors;

import io.benchuk.services.DataCollector;
import io.benchuk.models.ApiEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static io.benchuk.Consts.ANONYMOUS_SERVICE;
import static io.benchuk.Consts.SERVICE_NAME_HTTP_HEADER;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SpringRequestInterceptor extends HandlerInterceptorAdapter {

    private final DataCollector dataCollector;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object object, ModelAndView model)
            throws Exception {

        ApiEvent apiEvent = new ApiEvent();

        setFromService(request, apiEvent);

        setControllerData((HandlerMethod) object, apiEvent);
        Method method = handleMethodName((HandlerMethod) object, apiEvent);
        setMethodPathAndRestMethod(apiEvent, method);

        if (apiEvent.allValued()) {
            dataCollector.collectApiEvent(apiEvent);
            //System.out.println(apiEvent.toString());
        }
        else {
            //System.out.println("\n\nNOT ALL VALUED!!\n\n" + apiEvent.toString());
        }

    }

    private void setFromService(HttpServletRequest request, ApiEvent apiEvent) {
        String requestServiceName = request.getHeader(SERVICE_NAME_HTTP_HEADER);
        if (!StringUtils.isEmpty(requestServiceName)) {
            apiEvent.setFromService(ANONYMOUS_SERVICE);
        }
        apiEvent.setFromService(requestServiceName);
    }

    private Method handleMethodName(HandlerMethod object, ApiEvent apiEvent) {
        Method method = object.getMethod();
        apiEvent.setMethodName(method.getName());
        return method;
    }

    private void setMethodPathAndRestMethod(ApiEvent apiEvent, Method method) {
        Annotation[] annotations = method.getDeclaredAnnotations();
        for (Annotation a : annotations) {
            if (a instanceof GetMapping) {
                GetMapping getMapping = ((GetMapping) a);
                if (getMapping.value().length > 0) {
                    apiEvent.setMethodPath(getMapping.value()[0]);
                }
                apiEvent.setRestMethod(RequestMethod.GET.name());
            }
            if (a instanceof PostMapping) {
                PostMapping postMapping = ((PostMapping) a);
                if (postMapping.value().length > 0) {
                    apiEvent.setMethodPath(postMapping.value()[0]);
                }
                apiEvent.setRestMethod(RequestMethod.POST.name());
            }
            if (a instanceof DeleteMapping) {
                DeleteMapping deleteMapping = ((DeleteMapping) a);
                if (deleteMapping.value().length > 0) {
                    apiEvent.setMethodPath(deleteMapping.value()[0]);
                }
                apiEvent.setRestMethod(RequestMethod.DELETE.name());
            }
            if (a instanceof PutMapping) {
                PutMapping putMapping = ((PutMapping) a);
                if (putMapping.value().length > 0) {
                    apiEvent.setMethodPath(putMapping.value()[0]);
                }
                apiEvent.setRestMethod(RequestMethod.PUT.name());
            }
            if (a instanceof PatchMapping) {
                PatchMapping patchMapping = ((PatchMapping) a);
                if (patchMapping.value().length > 0) {
                    apiEvent.setMethodPath(patchMapping.value()[0]);
                }
                apiEvent.setRestMethod(RequestMethod.PATCH.name());
            }
            if (a instanceof RequestMapping) {
                RequestMapping requestMapping = ((RequestMapping) a);
                if (requestMapping.value().length > 0) {
                    apiEvent.setMethodPath(((RequestMapping) a).value()[0]);
                }
                if (requestMapping.method().length > 0) {
                    apiEvent.setRestMethod(requestMapping.method()[0].name());
                }
            }

        }
    }

    private void setControllerData(HandlerMethod object, ApiEvent apiEvent) {
        Annotation[] declaredAnnotations = object.getBeanType().getDeclaredAnnotations();
        apiEvent.setControllerClass(object.getBeanType().getName());

        for (Annotation a : declaredAnnotations) {
            if (a instanceof RequestMapping && ((RequestMapping) a).value().length > 0) {
                apiEvent.setControllerPath(((RequestMapping) a).value()[0]);
                break;
            }
        }
    }

}
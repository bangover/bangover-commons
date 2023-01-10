package cloud.bangover.functions;

import cloud.bangover.functions.BusinessFunction.Context;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "createFor")
public class MockFunctionRunner<Q, S> {
  private final BusinessFunction<Q, S> function;

  public Result<S> executeFunction() {
    return executeFunction(null);
  }

  public Result<S> executeFunction(Q request) {
    MockContext context = new MockContext(request);
    try {
      function.invoke(context);
    } catch (Exception error) {
      context.reject(error);
    }
    return context.getResult();
  }

  @Getter
  @RequiredArgsConstructor
  private class MockContext implements Context<Q, S> {
    private final Q request;
    private Result<S> result;

    @Override
    public void reply(S response) {
      this.result = () -> response;
    }

    @Override
    public void reject(Exception error) {
      this.result = () -> {
        throw error;
      };
    }

    public Result<S> getResult() {
      return result;
    }
  }

  public interface Result<S> {
    S getResult() throws Exception;
  }
}

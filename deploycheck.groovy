//input(message: 'Release Service?', id: 'release')
        userAborted = false
        startMillis = System.currentTimeMillis()
        timeoutMillis = 30000

        try {
          timeout(time: timeoutMillis, unit: 'MILLISECONDS') {
            input 'Do you approve?'
          }
        } 
        catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException e) 
        {
          cause = e.causes.get(0)
          echo cause.toString()
          echo "Aborted by " + cause.getUser().toString()
          if (cause.getUser().toString() != 'SYSTEM') {
            startMillis = System.currentTimeMillis()
          } else {
            endMillis = System.currentTimeMillis()
            if (endMillis - startMillis >= timeoutMillis) {
              echo "Approval timed out. Continuing with deployment."
            } else {
              userAborted = true
              echo "SYSTEM aborted, but looks like timeout period didn't complete. Aborting."
            }
          }
        }

        if (userAborted) 
        {
          currentBuild.result = 'ABORTED'
        }
        else 
        {
          currentBuild.result = 'SUCCESS'
          echo "Firing the missiles!"
        }
      
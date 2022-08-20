package yang.demo.allPurpose;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.DeleteBranchCommand;
import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.CannotDeleteCurrentBranchException;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NotMergedException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class gitTool
{
  public static void createGitRepository(String dirpath)
    throws IOException
  {
    Repository newlyCreatedRepo = FileRepositoryBuilder.create(new File(dirpath + "/.git"));
    newlyCreatedRepo.create();
  }
  
  public static void createNewBranch(String branchName, String respositoryPath)
    throws IOException, RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, GitAPIException
  {
    Git git = new Git(new FileRepository(respositoryPath + "/.git"));
    git.branchCreate()
      .setName(branchName)
      .setForce(true)
      .call();
  }
  
  public static void changeBranch(String respositoryPath, String branchName)
    throws RefAlreadyExistsException, RefNotFoundException, InvalidRefNameException, CheckoutConflictException, GitAPIException, IOException
  {
    Git git = new Git(new FileRepository(respositoryPath + "/.git"));
    Ref call = git.checkout().setName(branchName)
      .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
      .call();
  }
  
  public static Iterable<PushResult> pushToRemote(String respositoryPath, String host, String username, String password, boolean force)
    throws IOException, InvalidRemoteException, TransportException, GitAPIException
  {
    Git git = new Git(new FileRepository(respositoryPath + "/.git"));
    UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = 
      new UsernamePasswordCredentialsProvider(username, password);
    
    Iterable<PushResult> callresult = ((PushCommand)git.push().setRemote(host).setCredentialsProvider(usernamePasswordCredentialsProvider))
      .setForce(force)
      
      .call();
    return callresult;
  }
  
  public static void deleteBranchFromeLocal(String respositoryPath, String branchName)
    throws NotMergedException, CannotDeleteCurrentBranchException, GitAPIException, IOException
  {
    Git git = new Git(new FileRepository(respositoryPath + "/.git"));
    git.branchDelete().setBranchNames(new String[] { branchName })
      .setForce(true)
      .call();
  }
  
  public static void deleteBranchFromRemote(String respositoryPath, String host, String branchName, boolean deleteFromLocal, String username, String password)
    throws InvalidRemoteException, TransportException, GitAPIException, IOException
  {
    Git git = new Git(new FileRepository(respositoryPath + "/.git"));
    UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = 
      new UsernamePasswordCredentialsProvider(username, password);
    if (deleteFromLocal) {
      git.branchDelete().setBranchNames(new String[] { branchName }).setForce(true).call();
    }
    RefSpec refSpec = new RefSpec()
      .setSource(null)
      .setDestination(branchName);
    
    ((PushCommand)git.push().setRefSpecs(new RefSpec[] { refSpec }).setRemote(host).setCredentialsProvider(usernamePasswordCredentialsProvider))
      .setForce(true)
      .call();
  }
  
  public static Collection<Ref> getAllBranchFromRemote(String host, String username, String password)
    throws InvalidRemoteException, TransportException, GitAPIException
  {
    LsRemoteCommand remoteCommand = Git.lsRemoteRepository();
    Collection<Ref> refs = ((LsRemoteCommand)remoteCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password)))
      .setHeads(true)
      .setRemote(host)
      .call();
    return refs;
  }
  
  public static Iterable<RevCommit> getCommitInfoFromLocalRespository(String respositoryPath)
    throws NoHeadException, GitAPIException, IOException
  {
    Git git = new Git(new FileRepository(respositoryPath + "/.git"));
    Iterable<RevCommit> rev = git.log()
      .all()
      .call();
    return rev;
  }
  
  public static Git getGit(String respositoryPath)
    throws IOException
  {
    Git git = new Git(new FileRepository(respositoryPath + "/.git"));
    return git;
  }
  
  public static ArrayList<RevCommit> getCommitsFromBranch(String respositoryPath, String[] branchNames)
    throws IOException, NoHeadException, GitAPIException
  {
    Git git = getGit(respositoryPath);
    Repository repo = git.getRepository();
    RevWalk walk = new RevWalk(repo);
    ArrayList<RevCommit> al = new ArrayList();
    String[] arrayOfString;
    int j = (arrayOfString = branchNames).length;
    for (int i = 0; i < j; i++)
    {
      String branchName = arrayOfString[i];
      Iterable<RevCommit> commits = git.log().all().call();
      for (RevCommit commit : commits)
      {
        RevCommit targetCommit = walk.parseCommit(repo.resolve(
          commit.getName()));
        for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
          if (((String)e.getKey()).startsWith("refs/heads/")) {
            if (walk.isMergedInto(targetCommit, walk.parseCommit(((Ref)e.getValue()).getObjectId())))
            {
              String foundInBranch = ((Ref)e.getValue()).getName();
              if (branchName.equals(foundInBranch))
              {
                al.add(commit);
                break;
              }
            }
          }
        }
      }
    }
    return al;
  }
  
  public static List<DiffEntry> commit(String respositoryPath, String message)
    throws IOException, GitAPIException
  {
    Git git = new Git(new FileRepository(respositoryPath + "/.git"));
    List<DiffEntry> diff = git.diff().call();
    if ((diff != null) && (diff.size() == 0)) {
      return null;
    }
    List<String> updateFiles = new ArrayList();
    for (DiffEntry entry : diff)
    {
      DiffEntry.ChangeType changeType = entry.getChangeType();
      switch (changeType)
      {
      case ADD: 
      case COPY: 
      case MODIFY: 
      case RENAME: 
        updateFiles.add(entry.getNewPath());
        break;
      case DELETE: 
        updateFiles.add(entry.getOldPath());
      }
    }
    AddCommand addCmd = git.add();
    for (String file : updateFiles) {
      addCmd.addFilepattern(file);
    }
    addCmd.call();
    CommitCommand commit = git.commit();
    for (String file : updateFiles) {
      commit.setOnly(file);
    }
    commit.setMessage(message);
    commit.call();
    return diff;
  }
  
  public static void Pull(String localRespositoryPath, String host, String branchName, String username, String password)
    throws IOException, GitAPIException
  {
    UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = 
      new UsernamePasswordCredentialsProvider(username, password);
    
    Git git = new Git(new FileRepository(localRespositoryPath + "/.git"));
    
    ((PullCommand)git.pull().setRemote(host).setRemoteBranchName(branchName).setCredentialsProvider(usernamePasswordCredentialsProvider))
      .setRebase(true)
      .call();
  }
  
  public static void CloneFromRemote(String localPath, String host, boolean allBranch, Collection<String> branchs, String branchName, String username, String password)
    throws IOException, GitAPIException
  {
    UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = 
      new UsernamePasswordCredentialsProvider(username, password);
    
    CloneCommand cloneCommand = Git.cloneRepository();
    cloneCommand.setURI(host);
    if (!allBranch)
    {
      if (branchs != null) {
        cloneCommand.setBranchesToClone(branchs);
      } else {
        cloneCommand.setBranch(branchName);
      }
    }
    else {
      cloneCommand.setCloneAllBranches(true);
    }
    cloneCommand.setDirectory(new File(localPath));
    cloneCommand.setCredentialsProvider(usernamePasswordCredentialsProvider);
    cloneCommand.call();
  }
  
  public static void cloneFromLocal(String respositoryPath, String localPath, boolean allBranch, Collection<String> branchs, String branchName)
    throws IOException, InvalidRemoteException, TransportException, GitAPIException
  {
    CloneCommand cloneCommand = Git.cloneRepository();
    
    cloneCommand.setURI(respositoryPath);
    if (!allBranch)
    {
      if (branchs != null) {
        cloneCommand.setBranchesToClone(branchs);
      } else {
        cloneCommand.setBranch(branchName);
      }
    }
    else {
      cloneCommand.setCloneAllBranches(true);
    }
    cloneCommand.setDirectory(new File(localPath)).call();
  }
}
